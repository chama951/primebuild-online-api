package com.primebuild_online.controller;

import com.primebuild_online.model.Item;
import com.primebuild_online.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> saveItem(@RequestBody Item item) {
        return new ResponseEntity<>(itemService.saveItem(item), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Item> getAllItem() {
        return itemService.getAllItem();
    }

    @PutMapping("{id}")
    private ResponseEntity<Item> updateItemById(@PathVariable("id") long id, @RequestBody Item item) {
        return new ResponseEntity<Item>(itemService.updateItem(item,id),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") long id){
        return new ResponseEntity<Item>(itemService.getItemById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") long id){
        itemService.deleteItem(id);
        return new ResponseEntity<String>("Item deleted Successfully",HttpStatus.OK);
    }


}
