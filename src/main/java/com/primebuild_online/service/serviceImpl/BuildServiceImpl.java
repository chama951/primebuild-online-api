package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.User;
import com.primebuild_online.model.enumerations.BuildStatus;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.repository.BuildRepository;
import com.primebuild_online.security.SecurityUtils;
import com.primebuild_online.service.BuildItemService;
import com.primebuild_online.service.BuildService;
import com.primebuild_online.service.ItemService;
import com.primebuild_online.service.UserService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import com.primebuild_online.utils.validator.BuildValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BuildServiceImpl implements BuildService {
    private final ItemService itemService;
    private final BuildItemService buildItemService;
    private final BuildRepository buildRepository;
    private final BuildValidator buildValidator;
    private final UserService userService;

    public BuildServiceImpl(ItemService itemService,
                            BuildItemService buildItemService,
                            BuildRepository buildRepository,
                            BuildItemRepository buildItemRepository,
                            BuildValidator buildValidator,
                            UserService userService) {
        this.buildRepository = buildRepository;
        this.itemService = itemService;
        this.buildItemService = buildItemService;
        this.buildValidator = buildValidator;
        this.userService = userService;
    }

    private User loggedInUser() {
        return userService.getUserById(
                Objects.requireNonNull(SecurityUtils.getCurrentUser()).getId()
        );
    }

    @Override
    public Build saveBuildReq(BuildReqDTO buildReqDTO) {

        Build newBuild = new Build();

        newBuild.setCreatedAt(LocalDateTime.now());
        newBuild.setUser(loggedInUser());
        newBuild.setBuildName(buildReqDTO.getBuildName());
        newBuild.setBuildStatus(BuildStatus.valueOf(buildReqDTO.getBuildStatus()));
        newBuild.setCreatedDate(LocalDateTime.now());

        Build savedBuild = buildRepository.save(newBuild);

        if (buildReqDTO.getItemList() != null && !buildReqDTO.getItemList().isEmpty()) {
            newBuild = addNewBuildItems(buildReqDTO.getItemList(), savedBuild);
        }
        buildValidator.validate(newBuild);
        return buildRepository.save(newBuild);
    }

    //   FIXED SRP Violated by  itemService.saveItem(...)
    @Override
    public Build updateBuildReq(BuildReqDTO buildReqDTO, Long buildId) {

        Build buildInDb = buildRepository.findById(buildId)
                .orElseThrow(() -> new PrimeBuildException(
                        "Build not found",
                        HttpStatus.NOT_FOUND));

        buildInDb.setUpdatedAt(LocalDateTime.now());
        buildInDb.setUser(loggedInUser());
        buildInDb.setBuildName(buildReqDTO.getBuildName());
        buildInDb.setBuildStatus(BuildStatus.valueOf(buildReqDTO.getBuildStatus()));
        buildInDb.setLastModified(LocalDateTime.now());

        if (buildReqDTO.getBuildStatus().equals(BuildStatus.COMPLETED.toString())) {

            List<BuildItem> buildItemListByBuild = buildItemService.findAllByBuildId(buildId);

            for (BuildItem buildItem1 : buildItemListByBuild) {
                Item itemByBuildItem = itemService.getItemById(buildItem1.getItem().getId());
                Integer buildItemQuantity = buildItem1.getBuildQuantity();

                itemService.resetStockQuantity(itemByBuildItem, buildItemQuantity);
            }
        }

        buildInDb.getBuildItemList().clear();

        if (buildReqDTO.getItemList() != null && !buildReqDTO.getItemList().isEmpty()) {
            addNewBuildItems(buildReqDTO.getItemList(), buildInDb);
        }
        buildValidator.validate(buildInDb);
        return buildRepository.save(buildInDb);
    }

    //   FIXED SRP Violated by buildItemService.saveBuildItem(..)
    private Build addNewBuildItems(List<Item> itemList, Build build) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        for (Item itemRequest : itemList) {
            Item item = itemService.getItemById(itemRequest.getId());

            Integer itemQuantityToBuild = itemRequest.getQuantity();

            BigDecimal itemPrice = item.getPrice();

            BigDecimal subtotal = itemPrice
                    .multiply(BigDecimal.valueOf(itemQuantityToBuild));

            totalPrice = totalPrice.add(subtotal);

//            if (build.getBuildStatus().equals(BuildStatus.COMPLETED)) {
//
//                itemService.reduceItemQuantity(item, itemQuantityToBuild);
//            }

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
        buildRepository.deleteById(id);
    }

    @Override
    public Build getBuildById(Long id) {
        Optional<Build> build = buildRepository.findById(id);
        if (build.isPresent()) {
            return build.get();
        } else {
            throw new PrimeBuildException(
                    "Build not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<Build> getAllBuildsByCurrentUser() {
        return buildRepository.findAllByUser(loggedInUser());
    }

}
