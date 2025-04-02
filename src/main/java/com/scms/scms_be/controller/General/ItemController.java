package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.service.General.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/comad/create-item/{companyId}")
    public ResponseEntity<Item> create(@PathVariable Long companyId, @RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(companyId, item));
    }

    @GetMapping("/user/get-all-item-in-com/{companyId}")
    public ResponseEntity<List<Item>> getAll(@PathVariable Long companyId) {
        return ResponseEntity.ok(itemService.getAllItemsByCompany(companyId));
    }

    @GetMapping("/user/get-item/{itemId}")
    public ResponseEntity<Item> getById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @PutMapping("/comad/update-item/{itemId}")
    public ResponseEntity<Item> update(@PathVariable Long itemId, @RequestBody Item item) {
        return ResponseEntity.ok(itemService.updateItem(itemId, item));
    }
}
