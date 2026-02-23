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
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final ComponentService componentService;
    private final ManufacturerService manufacturerService;
    private final NotificationService notificationService;

    public ItemServiceImpl(ItemRepository itemRepository,
                           ItemValidator itemValidator,
                           ComponentService componentService,
                           ManufacturerService manufacturerService,
                           NotificationService notificationService) {

        this.itemRepository = itemRepository;
        this.itemValidator = itemValidator;
        this.componentService = componentService;
        this.manufacturerService = manufacturerService;
        this.notificationService = notificationService;
    }

    @Override
    public Item saveItemReq(ItemReqDTO itemReqDTO) {
        Item newItem = new Item();

        newItem = itemSetValues(itemReqDTO, newItem);

        return itemRepository.save(newItem);
    }

    public Item itemSetValues(ItemReqDTO itemReqDTO, Item item) {
        item.setItemName(itemReqDTO.getItemName());
        item.setQuantity(itemReqDTO.getQuantity());
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

        return itemRepository.save(itemInDb);
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
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
        Item item = getItemById(id);
        itemRepository.delete(item);
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
        lowStockNotification(itemInDb);
    }

    @Override
    public void resetItemStockQuantity(Item item, Integer quantityToAdd) {
        item.setQuantity(item.getQuantity() + quantityToAdd);
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
