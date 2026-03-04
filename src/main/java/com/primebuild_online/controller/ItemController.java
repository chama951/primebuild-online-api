package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.ItemReqDTO;
import com.primebuild_online.model.Item;
import com.primebuild_online.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> saveItemReq(@RequestBody ItemReqDTO itemReqDTO) {
        return new ResponseEntity<>(itemService.saveItemReq(itemReqDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    private ResponseEntity<Item> updateItemReq(@PathVariable("id") Long id, @RequestBody ItemReqDTO itemReqDTO) {
        return new ResponseEntity<Item>(itemService.updateItemReq(itemReqDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") Long id) {
        return new ResponseEntity<Item>(itemService.getItemById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteItem(@PathVariable("id") Long id) {
        itemService.deleteItem(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Item deleted Successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public Page<Item> getPaginatedItemList(@RequestParam(value = "component", required = false) Long componentId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "8") int size,
                                  @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);

        if (componentId != null) {
            return itemService.getPaginatedInStockItemListByComponent(componentId, pageable);
        }
        if (search != null) {
            return itemService.searchPaginatedItemsByName(search, pageable);
        } else {
            return itemService.getPaginatedAllItem(pageable);
        }

    }

    @GetMapping
    public List<Item> getItemList(){
       return itemService.getItemList();
    }

}
