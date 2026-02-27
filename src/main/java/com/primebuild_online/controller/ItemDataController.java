package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.ItemDataDTO;
import com.primebuild_online.model.ItemData;
import com.primebuild_online.model.enumerations.Vendors;
import com.primebuild_online.service.ItemDataService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item_data")
public class ItemDataController {

    private final ItemDataService itemDataService;

    public ItemDataController(ItemDataService itemDataService) {
        this.itemDataService = itemDataService;
    }

    @PostMapping
    public ResponseEntity<List<ItemData>> saveItemDataByItemIdAndVendor(@RequestBody ItemDataDTO itemDataDTO) {
        return new ResponseEntity<>(itemDataService.saveItemDataByVendor(itemDataDTO), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public List<ItemData> getItemDataByItemId(@PathVariable(value = "id") Long id,
                                              @RequestParam(value = "vendor", required = false) String vendor) {
        if (vendor != null) {
            return itemDataService.getItemDataByVendorAndItem(id, Vendors.valueOf(vendor));
        }
        return itemDataService.getItemDataByItemId(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteItemData(@PathVariable("id") Long id,
                                                              @RequestParam(value = "vendor", required = false) String vendor) {

        if (vendor != null) {
            itemDataService.deleteItemDataByVendor(id, Vendors.valueOf(vendor));
        } else {
            throw new PrimeBuildException(
                    vendor + " Item Data service not found",
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Item Data " + vendor.toLowerCase() + " deleted Successfully");

        return ResponseEntity.ok(response);
    }

}
