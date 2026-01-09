package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.Build;
import com.primebuild_online.model.BuildItem;
import com.primebuild_online.model.DTO.BuildRequestDTO;
import com.primebuild_online.model.DTO.ItemListDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.BuildStatus;
import com.primebuild_online.repository.BuildItemRepository;
import com.primebuild_online.repository.BuildRepository;
import com.primebuild_online.service.BuildItemService;
import com.primebuild_online.service.BuildService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BuildServiceImpl implements BuildService {

    private final ItemService itemService;

    private final BuildItemService buildItemService;

    List<BuildItem> buildItems = new ArrayList<>();

    private final BuildRepository buildRepository;

    private final BuildItemRepository buildItemRepository;

    public BuildServiceImpl(ItemService itemService, BuildItemService buildItemService, BuildRepository buildRepository, BuildItemRepository buildItemRepository) {
        this.buildRepository = buildRepository;
        this.buildItemRepository = buildItemRepository;
        this.itemService = itemService;
        this.buildItemService = buildItemService;
    }

    @Override
    public Build saveBuild(BuildRequestDTO buildRequest) {
        Build build = new Build();
        double totalPrice = 0;

        build.setBuildStatus(buildRequest.getBuildStatus());
        build.setCreatedDate(LocalDateTime.now());

        Build savedBuildId = buildRepository.save(build);

        // Adding New Items
        if (!buildRequest.getItems().isEmpty()) {
            build = addNewItem(buildRequest.getItems(), totalPrice, savedBuildId);
        }

        return buildRepository.save(build);
    }

    @Override
    public Build updateBuild(BuildRequestDTO buildRequest, long id) {
        Build existingBuild = buildRepository.findById(id).orElseThrow(RuntimeException::new);
        double totalPrice = existingBuild.getTotalPrice();

        // Updating Exist
        existingBuild.setBuildStatus(buildRequest.getBuildStatus());
        existingBuild.setLastModified(LocalDateTime.now());

        for (BuildItem buildItemRequest : buildRequest.getBuildItems()) {

            Item item = itemService.getItemById(buildItemRequest.getItem().getId());
            BuildItem exisingBuildItem = buildItemRepository.findById(buildItemRequest.getId()).orElseThrow(() -> new RuntimeException("BuildItem not found with id: " + buildItemRequest.getId()));

            if (!Objects.equals(exisingBuildItem.getBuildQuantity(), buildItemRequest.getBuildQuantity())) {
                double buildItemPrice = exisingBuildItem.getItem().getPrice();

//            Build Quantity and Item Quantity  at now
                Integer BuildQuantity = exisingBuildItem.getBuildQuantity();
                Integer itemQuantity = item.getQuantity();

//            Resetting the Total Quantity and Total Price
                totalPrice -= buildItemPrice * BuildQuantity;
                itemQuantity += BuildQuantity;

//            Set The quantity from request
                exisingBuildItem.setBuildQuantity(buildItemRequest.getBuildQuantity());
                BuildQuantity = buildItemRequest.getBuildQuantity();

//            updating the new Total Quantity and Total Price
                totalPrice += buildItemPrice * BuildQuantity;
                itemQuantity -= BuildQuantity;

                if (Objects.equals(existingBuild.getBuildStatus(), String.valueOf(BuildStatus.COMPLETED))) {
                    item.setQuantity(itemQuantity);
                }

                itemService.updateItem(item, item.getId());
                buildItemService.updateBuildItem(exisingBuildItem, buildItemRequest.getId());
            }

        }

        return buildRepository.save(addNewItem(buildRequest.getItems(), totalPrice, existingBuild));
    }

    private Build addNewItem(List<Item> itemList, Double totalPrice, Build build) {

        for (Item itemRequest : itemList) {
            Item item = itemService.getItemById(itemRequest.getId());
            totalPrice += item.getPrice();

            BuildItem buildItem = new BuildItem();
            buildItem.setBuild(build);
            buildItem.setItem(item);
            buildItem.setBuildQuantity(itemRequest.getQuantity());

            buildItems.add(buildItem);
            buildItemRepository.save(buildItem);

            if (Objects.equals(build.getBuildStatus(), String.valueOf(BuildStatus.COMPLETED))) {
                item.setQuantity(item.getQuantity() - itemRequest.getQuantity());
            }

            itemService.updateItem(item, item.getId());
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

    @Override
    public Build addNewItems(ItemListDTO itemListDTO, long id) {
        Build existingBuild = buildRepository.findById(id).orElseThrow(RuntimeException::new);
        double totalPrice = existingBuild.getTotalPrice();
        return buildRepository.save(addNewItem(itemListDTO.getItems(), totalPrice, existingBuild));
    }
}
