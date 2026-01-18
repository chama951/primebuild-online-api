package com.primebuild_online.service.serviceImpl;

import com.primebuild_online.model.DTO.BuildReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.model.ItemFeature;
import com.primebuild_online.service.CompatibilityService;
import com.primebuild_online.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompatibilityServiceImpl implements CompatibilityService {

    private final ItemService itemService;

    public CompatibilityServiceImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public List<Item> getCompatibleItemsByComponent(BuildReqDTO buildReqDTO, Long componentId) {
        List<Item> itemList = itemService.getInStockItemListByComponent(componentId);
        for (Item item : itemList) {
        }

        // getting selected items through request to compare and select compatible items to the response
        Item itemInReq;

        for (Item itemInReqId : buildReqDTO.getItemList()) {
            itemInReq = itemService.getItemById(itemInReqId.getId());

            lineBreaker(itemInReq.getComponent().getComponentName() + " : " + itemInReq.getItemName());

            for (ItemFeature itemFeature : itemInReq.getItemFeatureList()) {

                lineBreaker(itemFeature.getFeature().getFeatureType().getFeatureTypeName()
                        + " : " + itemFeature.getFeature().getFeatureName());

            }
        }
        return null;
    }


    public void lineBreaker(String string) {
        String horSeparator = "_____________________________________";
        string = horSeparator + "\n" + string + "\n" + horSeparator;
        System.out.println(string);
    }

}
