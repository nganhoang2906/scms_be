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

import com.scms.scms_be.model.dto.General.ManufactureLineDto;
import com.scms.scms_be.model.request.General.ManuLineRequest;
import com.scms.scms_be.service.General.ManufactureLineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManufactureLineController {

  @Autowired
  private ManufactureLineService lineService;

  @PostMapping("/comad/create-mf-line/{plantId}")
  public ResponseEntity<ManufactureLineDto> createLine(
      @PathVariable Long plantId,
      @RequestBody ManuLineRequest line) {
    return ResponseEntity.ok(lineService.createLine(plantId, line));
  }

  @GetMapping("/user/get-all-mf-line-in-com/{companyId}")
  public ResponseEntity<List<ManufactureLineDto>> getAllLinesInCompany(
      @PathVariable Long companyId) {
    return ResponseEntity.ok(lineService.getAllLinesInCompany(companyId));
  }

  @GetMapping("/user/get-mf-line/{lineId}")
  public ResponseEntity<ManufactureLineDto> getLineById(
      @PathVariable Long lineId) {
    return ResponseEntity.ok(lineService.getLineById(lineId));
  }

  @PutMapping("/comad/update-mf-line/{lineId}")
  public ResponseEntity<ManufactureLineDto> updateLine(
      @PathVariable Long lineId,
      @RequestBody ManuLineRequest line) {
    return ResponseEntity.ok(lineService.updateLine(lineId, line));
  }
  
}
