package com.primebuild_online.controller;

import com.primebuild_online.model.DTO.ItemComponentCountDTO;
import com.primebuild_online.model.ItemComponentCount;
import com.primebuild_online.service.ItemComponentCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item_component_count")
public class ItemComponentCountController {

    @Autowired
    private ItemComponentCountService itemComponentCountService;

    @PostMapping
    public ResponseEntity<ItemComponentCount> saveItemComponentCount(@RequestBody ItemComponentCountDTO itemComponentCountDTO) {
        return new ResponseEntity<>(itemComponentCountService.saveItemComponentCount(itemComponentCountDTO), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    private ResponseEntity<ItemComponentCount> updateItemComponentCount(@PathVariable("id") long id, @RequestBody ItemComponentCountDTO itemComponentCountDTO) {
        return new ResponseEntity<ItemComponentCount>(itemComponentCountService.updateItemComponentCount(itemComponentCountDTO, id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ItemComponentCount> getItemComponentCountById(@PathVariable("id") long id) {
        return new ResponseEntity<ItemComponentCount>(itemComponentCountService.getItemComponentCountById(id), HttpStatus.OK);
    }

    @GetMapping()
    public Object getAllItemComponentCount(
            @RequestParam(value = "item", required = false) Long itemId,
            @RequestParam(value = "component", required = false) Long componentId
    ) {
        if (componentId != null && itemId != null) {
            return itemComponentCountService.getByItemAndComponent(itemId, componentId);
        } else if (itemId != null) {
            return itemComponentCountService.getByItem(itemId);
        } else {
            return itemComponentCountService.getAllItemComponentCount();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deleteItemComponentCount(@PathVariable("id") long id) {
        itemComponentCountService.deleteItemComponentCount(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "item Component Count deleted Successfully");

        return ResponseEntity.ok(response);
    }

}
