package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.enumerations.BuildStatus;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.repository.BuildRepository;
import com.primebuild_online.service.BuildItemService;
import com.primebuild_online.service.BuildService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

        newBuild.setBuildStatus(BuildStatus.valueOf(buildReqDTO.getBuildStatus()));
        newBuild.setCreatedDate(LocalDateTime.now());

        Build savedBuild = buildRepository.save(newBuild);

        // Adding New Items
        if (buildReqDTO.getItemList() != null && !buildReqDTO.getItemList().isEmpty()) {
            newBuild = addNewBuildItems(buildReqDTO.getItemList(), savedBuild);
        }

        return buildRepository.save(newBuild);
    }

    //   FIXED SRP Violated by  itemService.saveItem(...)
    @Override
    public Build updateBuildReq(BuildReqDTO buildReqDTO, Long buildId) {
        Build buildInDb = buildRepository.findById(buildId).orElseThrow(() -> new PrimeBuildException(
                "Build not found",
                HttpStatus.NOT_FOUND));

        buildInDb.setBuildStatus(BuildStatus.valueOf(buildReqDTO.getBuildStatus()));
        buildInDb.setLastModified(LocalDateTime.now());

        List<BuildItem> buildItemListByBuild = buildItemService.findAllByBuildId(buildId);
//                updating Item quantity by adding into the stock
        for (BuildItem buildItem1 : buildItemListByBuild) {
            Item itemByBuildItem = itemService.getItemById(buildItem1.getItem().getId());
            Integer buildItemQuantity = buildItem1.getBuildQuantity();

            itemService.resetStockQuantity(itemByBuildItem, buildItemQuantity);
//            Integer itemExistingQuantity = itemByBuildItem.getQuantity();
//            itemByBuildItem.setQuantity(buildItemQuantity + itemExistingQuantity);
//            itemService.saveItem(itemByBuildItem);
//                    updating totalPrice after removing item by item
//            totalPrice -= itemByBuildItem.getPrice() * buildItemQuantity;
        }

//        buildItemService.deleteAllByBuildId(buildId);
        buildInDb.getBuildItemList().clear();

        if (buildReqDTO.getItemList() != null && !buildReqDTO.getItemList().isEmpty()) {
            addNewBuildItems(buildReqDTO.getItemList(), buildInDb);
        }
        return buildRepository.save(buildInDb);
    }

    //   FIXED SRP Violated by buildItemService.saveBuildItem(..)
    private Build addNewBuildItems(List<Item> itemList, Build build) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for (Item itemRequest : itemList) {
            Item item = itemService.getItemById(itemRequest.getId());


//            Integer itemStockQuantity = item.getQuantity();
            Integer itemQuantityToBuild = itemRequest.getQuantity();


//            int itemNewQuantity = itemStockQuantity - itemQuantityToBuild;

            BigDecimal itemPrice = item.getPrice();

            BigDecimal subtotal = itemPrice
                    .multiply(BigDecimal.valueOf(itemQuantityToBuild));

            totalPrice = totalPrice.add(subtotal);

            if (build.getBuildStatus().equals(BuildStatus.COMPLETED)) {
//                item.setQuantity(itemNewQuantity);
                itemService.reduceItemQuantity(item, itemQuantityToBuild);
            }

//            itemService.saveItem(item);

           BuildItem buildItem = buildItemService.createBuildItem(itemQuantityToBuild, item, build);

            build.getBuildItemList().add(buildItem);
        }


        build.setTotalPrice(totalPrice);
        return build;
    }

    @Override
    public List<Build> getAllBuild() {
        return buildRepository.findAll();
    }

    @Override
    public void deleteBuild(Long id) {
        buildRepository.findById(id).orElseThrow(RuntimeException::new);
        buildRepository.deleteById(id);
    }

    @Override
    public Build getBuildById(Long id) {
        Optional<Build> build = buildRepository.findById(id);
        if (build.isPresent()) {
            return build.get();
        } else {
            throw new RuntimeException();
        }
    }

}
