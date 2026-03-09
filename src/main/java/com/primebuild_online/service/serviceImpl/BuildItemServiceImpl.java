package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.enumerations.NotificationType;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.service.BuildItemService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.service.NotificationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BuildItemServiceImpl implements BuildItemService {
    private final BuildItemRepository buildItemRepository;
    private final ItemService itemService;
    private final NotificationService notificationService;

    public BuildItemServiceImpl(BuildItemRepository buildItemRepository,
                                @Lazy ItemService itemService,
                                NotificationService notificationService) {
        this.buildItemRepository = buildItemRepository;
        this.itemService = itemService;
        this.notificationService = notificationService;
    }

    @Override
    public BuildItem saveBuildItem(Item itemToAdd, Build build) {
        Item itemInDb = itemService.getItemById(itemToAdd.getId());

        itemService.checkItemsStockQuantity(itemInDb, itemToAdd);

        BuildItem buildItem = new BuildItem();
        buildItem.setBuildQuantity(itemToAdd.getQuantity());
        buildItem.setItem(itemInDb);
        buildItem.setUnitPrice(itemInDb.getPrice());
        buildItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(itemInDb, itemToAdd.getQuantity()));
        buildItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(itemInDb));
        buildItem.setSubtotal(itemService.calculateSubTotal(itemInDb, itemToAdd.getQuantity()));
        buildItem.setBuild(build);
        return buildItem;
    }

    @Override
    public void updateBuildItem(BuildItem buildItem, Long id) {
        BuildItem existingBuildItem = buildItemRepository.findById(id).orElseThrow(RuntimeException::new);
        existingBuildItem.setBuildQuantity(buildItem.getBuildQuantity());
    }

    @Override
    public BuildItem findByBuildIdAndItemId(Long buildId, Long itemId) {
        Optional<BuildItem> buildItem = buildItemRepository.findByBuildIdAndItemId(buildId, itemId);
        if (buildItem.isPresent()) {
            return buildItem.get();
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteAllByBuildId(Long buildId) {
        buildItemRepository.deleteAllByBuildId(buildId);
    }

    @Override
    public void deleteById(Long id) {
        buildItemRepository.deleteById(id);
    }

    @Override
    public List<BuildItem> findAllByBuildId(Long buildId) {
        return buildItemRepository.findAllByBuildId(buildId);
    }

    @Override
    public void resetItemQuantity(List<BuildItem> buildItemList) {
        for (BuildItem buildItem : buildItemList) {
            Item itemByBuildItem = itemService.getItemById(buildItem.getItem().getId());
            Integer buildItemQuantity = buildItem.getBuildQuantity();
            itemService.resetItemStockQuantity(itemByBuildItem, buildItemQuantity);
        }
    }

    @Override
    public boolean existsBuildItemByItem(Long id) {
        return buildItemRepository.existsByItem_Id(id);
    }

    @Override
    public void updateBuildItemAtPriceChange(List<BuildItem> buildItemList) {
        for (BuildItem buildItem : buildItemList) {
            if (buildItem.getUnitPrice().compareTo(buildItem.getItem().getPrice()) > 0) {
                notificationService.createNotification(
                        "Build Item",
                        buildItem.getItem().getItemName() + " Price has been reduced",
                        NotificationType.BUILD_ITEM_PRICE_REDUCED,
                        buildItem.getBuild().getUser());
            }
            buildItem.setUnitPrice(buildItem.getItem().getPrice());
            buildItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(buildItem.getItem(), buildItem.getBuildQuantity()));
            buildItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(buildItem.getItem()));

            buildItem.setSubtotal(itemService.calculateSubTotal(buildItem.getItem(), buildItem.getBuildQuantity()));
            buildItemRepository.save(buildItem);
        }
    }

    @Override
    public BigDecimal calculateDiscountAmount(List<BuildItem> buildItemList) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        for (BuildItem buildItem : buildItemList) {
            discountAmount = discountAmount.add(buildItem.getDiscountSubTotal());
        }
        return discountAmount;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<BuildItem> buildItemList) {
        BigDecimal TotalAmount = BigDecimal.ZERO;
        for (BuildItem buildItem : buildItemList) {
            TotalAmount = TotalAmount.add(buildItem.getSubtotal());
        }
        return TotalAmount;
    }

}
