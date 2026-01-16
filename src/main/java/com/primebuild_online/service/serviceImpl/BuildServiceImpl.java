package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.BuildStatus;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.repository.BuildRepository;
import com.primebuild_online.service.BuildItemService;
import com.primebuild_online.service.BuildService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BuildServiceImpl implements BuildService {
    private final ItemService itemService;
    private final BuildItemService buildItemService;
    private final BuildRepository buildRepository;

    public BuildServiceImpl(ItemService itemService, BuildItemService buildItemService, BuildRepository buildRepository, BuildItemRepository buildItemRepository) {
        this.buildRepository = buildRepository;
        this.itemService = itemService;
        this.buildItemService = buildItemService;
    }

    @Override
    public Build saveBuildReq(BuildReqDTO buildReqDTO) {
        Build newBuild = new Build();

        newBuild.setBuildStatus(buildReqDTO.getBuildStatus());
        newBuild.setCreatedDate(LocalDateTime.now());

        Build savedBuild = buildRepository.save(newBuild);

        // Adding New Items
        if (buildReqDTO.getItemList() != null && !buildReqDTO.getItemList().isEmpty()) {
            newBuild = addNewBuildItems(buildReqDTO.getItemList(), savedBuild);
        }

        return buildRepository.save(newBuild);
    }

    @Override
    public Build updateBuildReq(BuildReqDTO buildReqDTO, Long buildId) {
        Build buildInDb = buildRepository.findById(buildId).orElseThrow(RuntimeException::new);

        buildInDb.setBuildStatus(buildReqDTO.getBuildStatus());
        buildInDb.setLastModified(LocalDateTime.now());

        List<BuildItem> buildItemListByBuild = buildItemService.findAllByBuildId(buildId);
//                updating Item quantity by adding into the stock
        for (BuildItem buildItem1 : buildItemListByBuild) {
            Item itemByBuildItem = itemService.getItemById(buildItem1.getItem().getId());
            Integer buildItemQuantity = buildItem1.getBuildQuantity();
            Integer itemExistingQuantity = itemByBuildItem.getQuantity();
            itemByBuildItem.setQuantity(buildItemQuantity + itemExistingQuantity);
            itemService.saveItem(itemByBuildItem);
//                    updating totalPrice after removing item by item
//            totalPrice -= itemByBuildItem.getPrice() * buildItemQuantity;
        }

        buildItemService.deleteAllByBuildId(buildId);

        if (buildReqDTO.getItemList() != null && !buildReqDTO.getItemList().isEmpty()) {
            addNewBuildItems(buildReqDTO.getItemList(), buildInDb);
        }
        return buildRepository.save(buildInDb);
    }

    private Build addNewBuildItems(List<Item> itemList, Build build) {
        double totalPrice = 0;

        for (Item itemRequest : itemList) {
            Item item = itemService.getItemById(itemRequest.getId());

            Integer itemStockQuantity = item.getQuantity();
            Integer itemQuantityToAdd = itemRequest.getQuantity();

            int itemNewQuantity = itemStockQuantity - itemQuantityToAdd;

            double itemPrice = item.getPrice();

            totalPrice += itemPrice * itemQuantityToAdd;

            if (Objects.equals(build.getBuildStatus(), String.valueOf(BuildStatus.COMPLETED))) {
                item.setQuantity(itemNewQuantity);
            }

            itemService.saveItem(item);

            BuildItem buildItem = new BuildItem();
            buildItem.setBuildQuantity(itemQuantityToAdd);
            buildItem.setItem(item);
            buildItem.setBuild(build);
            buildItemService.saveBuildItem(buildItem);
        }


        build.setTotalPrice(totalPrice);
        return build;
    }

    @Override
    public List<Build> getAllBuild() {
        return buildRepository.findAll();
    }

    @Override
    public void deleteBuild(long id) {
        buildRepository.findById(id).orElseThrow(RuntimeException::new);
        buildRepository.deleteById(id);
    }

    @Override
    public Build getBuildById(long id) {
        Optional<Build> build = buildRepository.findById(id);
        if (build.isPresent()) {
            return build.get();
        } else {
            throw new RuntimeException();
        }
    }

}
