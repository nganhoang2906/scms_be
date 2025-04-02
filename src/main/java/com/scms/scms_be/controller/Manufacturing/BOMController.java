package com.scms.scms_be.controller.Manufacturing;

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

import com.scms.scms_be.model.dto.request.BOMRequest;
import com.scms.scms_be.model.entity.Manufaacturing.BOM;
import com.scms.scms_be.model.entity.Manufaacturing.BOMDetail;
import com.scms.scms_be.service.Manufacturing.BOMService;

@RestController
public class BOMController {

    @Autowired
    private BOMService bomService;

    @PostMapping("/user/create-bom")
    public ResponseEntity<BOM> createBOM(@RequestBody BOMRequest request) {
        BOM created = bomService.createBOM(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/all-bom-in-com/{companyId}")
    public ResponseEntity<List<BOM>> getAllBOMsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(bomService.getAllBOMInCom(companyId));
    }

    @GetMapping("/user/get-bom/{bomId}")
    public ResponseEntity<BOM> getBOMById(@PathVariable Long bomId) {
        return ResponseEntity.ok(bomService.getBOMById(bomId));
    }

    @PutMapping("/user/update-bom/{bomId}")
    public ResponseEntity<BOM> updateBOM(@PathVariable Long bomId, @RequestBody BOMRequest bom) {
        return ResponseEntity.ok(bomService.updateBOM(bomId, bom));
    }

    @DeleteMapping("/comad/delete-bom/{bomId}")
    public ResponseEntity<String> deleteBOM(@PathVariable Long bomId) {
        bomService.deleteBOM(bomId);
        return ResponseEntity.ok("Đã xoá BOM");
    }

    // BOM Detail
    @PostMapping("/user/add-bom-detail/{bomId}")
    public ResponseEntity<BOMDetail> addBOMDetail(@PathVariable Long bomId, @RequestBody BOMRequest.BOMDetailDTO request) {
        BOMDetail created = bomService.addBomDetail(bomId, request);
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/user/update-bom-detail/{bomId}/{bomDetailId}")
    public ResponseEntity<BOMDetail> updateBOMDetail(@PathVariable Long bomId, @PathVariable Long bomDetailId, @RequestBody BOMRequest.BOMDetailDTO request) {
        return ResponseEntity.ok(bomService.updateBOMDetail(bomId, bomDetailId, request));
    }
    @DeleteMapping("/user/delete-bom-detail/{bomDetailId}")
    public ResponseEntity<String> deleteBOMDetail(@PathVariable Long bomDetailId) {
        bomService.deleteBOMDetail(bomDetailId);
        return ResponseEntity.ok("Đã xoá BOM Detail");
    }

    
}
