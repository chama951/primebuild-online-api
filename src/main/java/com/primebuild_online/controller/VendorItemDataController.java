package com.primebuild_online.controller;

import com.primebuild_online.model.ItemData;
import com.primebuild_online.service.VendorItemDataService;
import com.primebuild_online.utils.exception.PrimeBuildException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/vendor_item_data")
public class VendorItemDataController {

    private final VendorItemDataService vendorItemDataService;

    public VendorItemDataController(VendorItemDataService vendorItemDataService) {
        this.vendorItemDataService = vendorItemDataService;
    }

    @GetMapping
    public ResponseEntity<ItemData> getItemDataByItemIdAndVendor(@RequestParam(value = "item_id", required = false) Long itemId,
                                                                 @RequestParam(value = "vendor_name", required = false) String vendorName) {

        if (Objects.equals(vendorName.toLowerCase(), "nanotek")) {
            return new ResponseEntity<ItemData>
                    (vendorItemDataService.nanotekItemData(itemId), HttpStatus.OK);
        } else {
            throw new PrimeBuildException(
                    vendorName + " Item Data service not found",
                    HttpStatus.NOT_FOUND
            );
        }

    }


}
