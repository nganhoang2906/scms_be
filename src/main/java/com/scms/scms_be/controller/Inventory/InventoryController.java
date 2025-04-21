package com.scms.scms_be.controller.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.Inventory.InventoryDto;
import com.scms.scms_be.model.request.Inventory.InventoryRequest;
import com.scms.scms_be.model.request.Inventory.putItemToInventoryRequest;
import com.scms.scms_be.service.Inventory.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/user/create-inventory")
    public ResponseEntity<InventoryDto> createInventory(@RequestBody InventoryRequest inventory) {
        return ResponseEntity.ok(inventoryService.createInventory(inventory));
    }

    @PostMapping("/user/put-item-to-inventory")
    public ResponseEntity<InventoryDto> putItemToInventory(@RequestBody putItemToInventoryRequest request) {
        return ResponseEntity.ok(inventoryService.putItemToInventory(request));
    }

    @PostMapping("/user/increase-ondemand/{inventoryId}")
    public ResponseEntity<InventoryDto> increaseOnDemand(
            @PathVariable Long inventoryId,
            @RequestParam Double amount) {
        return ResponseEntity.ok(inventoryService.increaseOnDemand(inventoryId, amount));
    }

    @PostMapping("/user/consume-ondemand/{inventoryId}")
    public ResponseEntity<InventoryDto> consumeOnDemand(
            @PathVariable Long inventoryId,
            @RequestParam Double amount) {
        return ResponseEntity.ok(inventoryService.consumeOnDemand(inventoryId, amount));
    }

    @GetMapping("/user/get-all-inventory/{companyId}/{itemId}/{warehouseId}")
    public ResponseEntity<List<InventoryDto>> getAllByItem(
            @PathVariable Long companyId,
            @PathVariable Long itemId,
            @PathVariable Long warehouseId) {
        return ResponseEntity.ok(inventoryService.getInventoryByItemAndWarehouse( companyId, itemId, warehouseId));
    }

    @GetMapping("/user/check-inventory/{itemId}/{warehouseId}")
    public ResponseEntity<Object> checkInventory(
            @PathVariable Long itemId,
            @PathVariable Long warehouseId,
            @RequestParam Double amount) {
        return ResponseEntity.ok(inventoryService.checkInventory(itemId, warehouseId, amount));
    }


    @GetMapping("/user/get-inventory/{inventoryId}")
    public ResponseEntity<InventoryDto> getById(
            @PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @PutMapping("/user/update-inventory/{inventoryId}")
    public ResponseEntity<InventoryDto> updateInventory(
            @PathVariable Long inventoryId, 
            @RequestBody InventoryRequest inventory) {
        return ResponseEntity.ok(inventoryService.updateInventory(inventoryId, inventory));
    }

}
