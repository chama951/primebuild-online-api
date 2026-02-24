package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.*;
import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.repository.ItemRepository;
import com.primebuild_online.service.*;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.ItemValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final ComponentService componentService;
    private final ManufacturerService manufacturerService;
    private final NotificationService notificationService;
    private final ItemDataHistoryService itemDataHistoryService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final InvoiceService invoiceService;
    private final InvoiceItemService invoiceItemService;
    private final BuildItemService buildItemService;
    private final ItemAnalyticsService itemAnalyticsService;

    public ItemServiceImpl(ItemRepository itemRepository,
                           ItemValidator itemValidator,
                           ComponentService componentService,
                           ManufacturerService manufacturerService,
                           NotificationService notificationService,
                           ItemDataHistoryService itemDataHistoryService,
                           CartService cartService,
                           CartItemService cartItemService,
                           InvoiceService invoiceService, InvoiceItemService invoiceItemService,
                           BuildItemService buildItemService, ItemAnalyticsService itemAnalyticsService) {

        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
        this.componentService = componentService;
        this.manufacturerService = manufacturerService;
        this.notificationService = notificationService;
        this.itemDataHistoryService = itemDataHistoryService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.invoiceService = invoiceService;
        this.invoiceItemService = invoiceItemService;
        this.itemAnalyticsService = itemAnalyticsService;
        ;
        this.buildItemService = buildItemService;
    }

    @Override
    public Item saveItemReq(ItemReqDTO itemReqDTO) {
        Item newItem = new Item();

        newItem = itemSetValues(itemReqDTO, newItem);

        newItem = itemRepository.save(newItem);

        itemDataHistoryService.saveItemDataHistory(
                newItem,
                newItem.getPrice(),
                null
        );

        itemAnalyticsService.saveItemAnalytics(newItem);

        return newItem;
    }

    public Item itemSetValues(ItemReqDTO itemReqDTO, Item item) {
        item.setItemName(itemReqDTO.getItemName());
        item.setQuantity(itemReqDTO.getQuantity());

        if (item.getPrice() != null &&
                !item.getPrice().equals(
                        itemReqDTO.getPrice().setScale(
                                2, RoundingMode.HALF_UP))) {
            itemDataHistoryService.saveItemDataHistory(
                    item,
                    itemReqDTO.getPrice(),
                    null
            );

        }

        item.setPrice(itemReqDTO.getPrice());
        item.setPowerConsumption(itemReqDTO.getPowerConsumption());
        item.setDiscountPercentage(itemReqDTO.getDiscountPercentage());

        if (itemReqDTO.getDiscountPercentage() == null) {
            item.setDiscountPercentage(BigDecimal.valueOf(0));
        }

        if (itemReqDTO.getComponentId() != null) {
            Component component = componentService.getComponentById(itemReqDTO.getComponentId());
            item.setComponent(component);
        }

        if (itemReqDTO.getManufacturerId() != null) {
            Manufacturer manufacturer = manufacturerService.getManufacturerById(itemReqDTO.getManufacturerId());
            item.setManufacturer(manufacturer);
        }

        if (item.getId() == null &&
                item.getComponent() != null &&
                itemRepository.existsByItemNameIgnoreCaseAndComponentId(
                        item.getItemName(),
                        item.getComponent().getId())) {

            throw new PrimeBuildException(
                    "Item already exists for this component",
                    HttpStatus.CONFLICT
            );
        }
        lowStockNotification(item);
        itemValidator.validate(item);
        return item;
    }

    @Override
    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public Item updateItemReq(ItemReqDTO itemReqDTO, Long id) {
        Item itemInDb = itemRepository.findById(id).orElseThrow(
                () -> new PrimeBuildException(
                        "Item not found",
                        HttpStatus.NOT_FOUND));
        itemInDb = itemSetValues(itemReqDTO, itemInDb);

        itemInDb = itemRepository.save(itemInDb);

        if (!itemAnalyticsService.existsItemAnalyticsByItem(id)) {
            itemAnalyticsService.saveItemAnalytics(itemInDb);
        }

        cartItemService.updateCartItemAtPriceChange(itemInDb.getId());

        buildItemService.updateBuildItemAtPriceChange(itemInDb.getId());

        return itemInDb;
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.getItemsById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new PrimeBuildException(
                    "Item not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteItem(Long id) {
        if (invoiceItemService.existsInvoiceByItem(id)) {
            throw new PrimeBuildException(
                    "Item cannot be deleted while found in Invoices",
                    HttpStatus.CONFLICT);
        }
        if (cartItemService.existsCartItemByItem(id)) {
            throw new PrimeBuildException(
                    "Item cannot be deleted while found in Carts",
                    HttpStatus.CONFLICT);
        }
        if (buildItemService.existsBuildItemByItem(id)) {
            throw new PrimeBuildException(
                    "Item cannot be deleted while found in Builds",
                    HttpStatus.CONFLICT);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> getInStockItemListByComponent(Long componentId) {
        return itemRepository.findByQuantityGreaterThanAndComponentId(0, componentId);
    }

    @Override
    public List<Item> getItemsByIds(List<Long> ids) {
        return itemRepository.findAllById(ids);
    }

    @Override
    public void reduceItemQuantity(Item itemInDb, Integer quantityToReduce) {
        Integer reduceQuantity = itemInDb.getQuantity() - quantityToReduce;
        itemInDb.setQuantity(reduceQuantity);
        itemInDb = itemRepository.save(itemInDb);
        itemAnalyticsService.atReduceItemQuantity(itemInDb, quantityToReduce);
        lowStockNotification(itemInDb);
    }

    @Override
    public void resetItemStockQuantity(Item item, Integer quantityToAdd) {
        item.setQuantity(item.getQuantity() + quantityToAdd);
        itemAnalyticsService.atResetItemQuantity(item, quantityToAdd);
        itemRepository.save(item);
    }

    @Override
    public void checkItemsListStockQuantity(List<Item> itemList) {
        for (Item itemRequest : itemList) {
            Item itemInDb;
            itemInDb = getItemById(itemRequest.getId());
            Integer requestedQnatity = itemRequest.getQuantity();
            if (itemInDb.getQuantity() < requestedQnatity) {
                throw new PrimeBuildException(
                        itemInDb.getItemName() + " Insufficient Stock",
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public void checkItemsStockQuantity(Item itemInDb, Item itemToAdd) {
        if (itemInDb.getQuantity() < itemToAdd.getQuantity()) {
            throw new PrimeBuildException(
                    itemInDb.getItemName() + " Insufficient Stock",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public BigDecimal calculateDiscountSubTotal(Item itemInDb, int quantity) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));

        BigDecimal discountSubTotal = discountAmount
                .multiply(BigDecimal.valueOf(quantity));

        return discountSubTotal;
    }

    @Override
    public BigDecimal calculateSubTotal(Item itemInDb, int quantity) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));

        BigDecimal subTotal = itemPrice.subtract(discountAmount)
                .multiply(BigDecimal.valueOf(quantity));
        return subTotal;
    }

    @Override
    public BigDecimal calculateDiscountPerUnite(Item itemInDb) {
        BigDecimal itemPrice = itemInDb.getPrice();
        BigDecimal discountPercentage = itemInDb.getDiscountPercentage();

        BigDecimal discountAmount = itemPrice
                .multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));
        return discountAmount;
    }

    @Override
    public boolean existsItemByManufacturer(Long id) {
        return itemRepository.existsByManufacturer_Id(id);
    }

    @Override
    public boolean existsItemByComponent(Long id) {
        return itemRepository.existsByComponent_Id(id);
    }

    public void lowStockNotification(Item item) {
        if (item.getQuantity() <= item.getLowStockThreshold()) {
            notificationService.createNotification(
                    "Low Stock Alert",
                    item.getItemName() + " is running low",
                    NotificationType.LOW_STOCK
            );
        }
    }
}
