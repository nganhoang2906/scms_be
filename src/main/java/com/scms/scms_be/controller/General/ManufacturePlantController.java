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

import com.scms.scms_be.model.dto.General.ManufacturePlantDto;
import com.scms.scms_be.model.request.General.ManuPlantRequest;
import com.scms.scms_be.service.General.ManufacturePlantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManufacturePlantController {

    @Autowired
    private ManufacturePlantService plantService;

    @PostMapping("/comad/create-mf-plant/{companyId}")
    public ResponseEntity<ManufacturePlantDto> createPlant(
            @PathVariable Long companyId,
            @RequestBody ManuPlantRequest plant) {
        return ResponseEntity.ok(plantService.createPlant(companyId, plant));
    }

    @GetMapping("/user/get-all-mf-plant-in-com/{companyId}")
    public ResponseEntity<List<ManufacturePlantDto>> getAllPlants(@PathVariable Long companyId) {
        return ResponseEntity.ok(plantService.getAllPlantsInCompany(companyId));
    }

    @GetMapping("/user/get-mf-plant/{plantId}")
    public ResponseEntity<ManufacturePlantDto> getPlantById(@PathVariable Long plantId) {
        return ResponseEntity.ok(plantService.getPlantById(plantId));
    }

    @PutMapping("/comad/update-mf-plant/{plantId}")
    public ResponseEntity<ManufacturePlantDto> updatePlant(
            @PathVariable Long plantId,
            @RequestBody ManuPlantRequest plant) {
        return ResponseEntity.ok(plantService.updatePlant(plantId, plant));
    }
}
