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

import com.scms.scms_be.model.dto.General.WarehouseDto;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.service.General.WarehouseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/comad/add-warehouse/{companyId}")
    public ResponseEntity<WarehouseDto> createWarehouse(@PathVariable Long companyId, @RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.createWarehouse(companyId, warehouse));
    }

    @GetMapping("/user/get-warehouse/{warehouseId}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(warehouseId));
    }

    @GetMapping("/user/get-all-warehouse-in-company/{companyId}")
    public ResponseEntity<List<WarehouseDto>> getAllWarehouseInCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(warehouseService.getAllWarehousesInCompany(companyId));
    }

    @PutMapping("/comad/update-warehouse/{warehouseId}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable Long warehouseId, @RequestBody Warehouse updatedWarehouse) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(warehouseId, updatedWarehouse));
    }

}
