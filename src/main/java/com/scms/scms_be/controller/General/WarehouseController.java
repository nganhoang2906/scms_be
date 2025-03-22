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

import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.service.General.WarehouseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/comad/add-warehouse/{companyId}")
    public ResponseEntity<Warehouse> createWarehouse(@PathVariable Long companyId ,@RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.createWarehouse(companyId,warehouse));
    }

    @GetMapping("/comad/get-warehouse/{warehouseId}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(warehouseId));
    }

    @GetMapping("/comad/get-all-warehouse-in-company/{companyId}")
    public ResponseEntity<List<Warehouse>> getAllWarehouseInCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(warehouseService.getAllWarehousesInCompany(companyId));
    }

    @PutMapping("/comad/update-warehouse/{warehouseId}")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long warehouseId, @RequestBody Warehouse updatedWarehouse) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(warehouseId, updatedWarehouse));
    }
  
}
