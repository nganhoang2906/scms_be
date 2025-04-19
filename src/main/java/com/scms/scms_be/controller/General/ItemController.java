package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.General.ItemDto;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.request.General.ItemRequest;
import com.scms.scms_be.service.General.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/comad/create-item/{companyId}")
    public ResponseEntity<ItemDto> createItem(@PathVariable Long companyId, @RequestBody ItemRequest item) {
        ItemDto createdItem = itemService.createItem(companyId, item);
        return ResponseEntity.ok(createdItem);
    }

    @GetMapping("/user/get-all-item-in-com/{companyId}")
    public ResponseEntity<List<ItemDto>> getAll(@PathVariable Long companyId) {
        List<ItemDto> items = itemService.getAllItemsInCompany(companyId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/user/get-item/{itemId}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long itemId) {
        ItemDto item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/comad/update-item/{itemId}")
    public ResponseEntity<ItemDto> update(@PathVariable Long itemId, @RequestBody Item item) {
        ItemDto updatedItem = itemService.updateItem(itemId, item);
        return ResponseEntity.ok(updatedItem);
    }
    
    @DeleteMapping("/comad/delete-item/{itemId}")
    public ResponseEntity<String> delete(@PathVariable Long itemId) {
        boolean deleted = itemService.deleteItem(itemId);
        if (deleted) {
            return ResponseEntity.ok("Item đã được xóa thành công.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
