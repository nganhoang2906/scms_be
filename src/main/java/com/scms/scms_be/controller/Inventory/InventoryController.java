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

  @PostMapping("/user/increase-quantity")
  public ResponseEntity<InventoryDto> increaseQuantity(@RequestBody InventoryRequest request) {
    return ResponseEntity.ok(inventoryService.increaseQuantity(request));
  }

  @PostMapping("/user/decrease-quantity")
  public ResponseEntity<InventoryDto> decreaseQuantity(@RequestBody InventoryRequest request) {
    return ResponseEntity.ok(inventoryService.decreaseQuantity(request));
  }

  @PostMapping("/user/increase-ondemand")
  public ResponseEntity<InventoryDto> increaseOnDemand(@RequestBody InventoryRequest request) {
    return ResponseEntity.ok(inventoryService.increaseOnDemand(request));
  }

  @PostMapping("/user/decrease-ondemand")
  public ResponseEntity<InventoryDto> decreaseOnDemand(@RequestBody InventoryRequest request) {
    return ResponseEntity.ok(inventoryService.decreaseOnDemand(request));
  }

  @GetMapping("/user/get-all-inventory/{companyId}/{itemId}/{warehouseId}")
  public ResponseEntity<List<InventoryDto>> getAllByItem(
      @PathVariable Long companyId,
      @PathVariable Long itemId,
      @PathVariable Long warehouseId) {
    return ResponseEntity.ok(inventoryService.getInventoryByItemAndWarehouse(companyId, itemId, warehouseId));
  }

}
