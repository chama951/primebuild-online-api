package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.CartItem;
import com.primebuild_online.model.Item;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.service.BuildItemService;
import com.primebuild_online.service.ItemService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildItemServiceImpl implements BuildItemService {
    private final BuildItemRepository buildItemRepository;
    private final ItemService itemService;

    public BuildItemServiceImpl(BuildItemRepository buildItemRepository,
                                @Lazy ItemService itemService) {
        this.buildItemRepository = buildItemRepository;
        this.itemService = itemService;
    }

    @Override
    public BuildItem saveBuildItem(Item itemToAdd, Build build) {
        Item itemInDb = itemService.getItemById(itemToAdd.getId());

        itemService.checkItemsStockQuantity(itemInDb, itemToAdd);

        BuildItem buildItem = new BuildItem();
        buildItem.setBuildQuantity(itemToAdd.getQuantity());
        buildItem.setItem(itemInDb);
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
        List<BuildItem> buildItemList = buildItemRepository.findAllByBuildId(buildId);
        return buildItemList;
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
    public void updateBuildItemAtPriceChange(Long id) {
        Item itemInDb = itemService.getItemById(id);
        List<BuildItem> buildItemList = buildItemRepository.findAllByItem_Id(id);
        for (BuildItem buildItem : buildItemList) {
            buildItem.setUnitPrice(itemInDb.getPrice());
            buildItem.setDiscountSubTotal(itemService.calculateDiscountSubTotal(itemInDb, buildItem.getBuildQuantity()));
            buildItem.setDiscountPerUnite(itemService.calculateDiscountPerUnite(itemInDb));
            buildItem.setSubtotal(itemService.calculateSubTotal(itemInDb, buildItem.getBuildQuantity()));
            buildItemRepository.save(buildItem);
        }
    }


}
