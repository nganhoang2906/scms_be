package com.scms.scms_be.controller.General;

import com.scms.scms_be.model.entity.General.ManufactureLine;
import com.scms.scms_be.service.General.ManufactureLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class ManufactureLineController {

    @Autowired
    private ManufactureLineService lineService;

    // Tạo ManufactureLine
    @PostMapping("/comad/create-mf-line/{plantId}")
    public ResponseEntity<ManufactureLine> createLine(
            @PathVariable Long plantId,
            @RequestBody ManufactureLine line) {
        return ResponseEntity.ok(lineService.createLine(plantId, line));
    }

    // Lấy tất cả ManufactureLine của một nhà máy
    @GetMapping("/comad/all-mf-line-in-plant/{plantId}")
    public ResponseEntity<List<ManufactureLine>> getAllLines(@PathVariable Long plantId) {
        return ResponseEntity.ok(lineService.getAllLinesInPlant(plantId));
    }

    // Lấy ManufactureLine theo ID
    @GetMapping("/comad/get-mf-line/{lineId}")
    public ResponseEntity<ManufactureLine> getLineById(@PathVariable Long lineId) {
        return ResponseEntity.ok(lineService.getLineById(lineId));
    }

    // Cập nhật ManufactureLine
    @PutMapping("/comad/update-mf-line/{lineId}")
    public ResponseEntity<ManufactureLine> updateLine(
            @PathVariable Long lineId,
            @RequestBody ManufactureLine line) {
        return ResponseEntity.ok(lineService.updateLine(lineId, line));
    }
}
