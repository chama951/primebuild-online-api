package com.primebuild_online.controller;

import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import com.primebuild_online.service.ItemDataService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item_data")
public class ItemDataController {

    private final ItemDataService itemDataService;

    public ItemDataController(ItemDataService itemDataService) {
        this.itemDataService = itemDataService;
    }

    @GetMapping
    public List<ItemData> getItemDataByItemIdAndVendor(@RequestParam(value = "item_id", required = false) Long itemId,
                                                       @RequestParam(value = "vendor", required = false) String vendor) {

        if (vendor != null && vendor.equals(Vendors.NANOTEK.toString().toLowerCase())) {
            return itemDataService.nanotekItemData(itemId, Vendors.NANOTEK);
        } else {
            throw new PrimeBuildException(
                    vendor + " Item Data service not found",
                    HttpStatus.NOT_FOUND
            );
        }

    }

    @GetMapping("{id}")
    public List<ItemData> getItemDataByItemId(@PathVariable(value = "id") Long id){
        return itemDataService.getItemDataByItemId(id);
    }

}
