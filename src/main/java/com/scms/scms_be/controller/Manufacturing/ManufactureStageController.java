package com.scms.scms_be.controller.Manufacturing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.Manufacture.ManufactureStageDto;
import com.scms.scms_be.model.request.Manufacturing.ManuStageRequest;
import com.scms.scms_be.service.Manufacturing.ManufactureStageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManufactureStageController {

    @Autowired 
    private  ManufactureStageService stageService;

    @PostMapping("/user/create-stage/{itemId}")
    public ResponseEntity<ManufactureStageDto> createStage(
            @RequestBody ManuStageRequest stage) {
        return ResponseEntity.ok(stageService.createStage(stage));
    }

    @GetMapping("/user/get-stage/{itemId}")
    public ResponseEntity<ManufactureStageDto> getStagesByItemId(
            @PathVariable Long itemId) {
        return ResponseEntity.ok(stageService.getStagesByItemId(itemId));
    }

    @GetMapping("/user/get-stage/{stageId}")
    public ResponseEntity<ManufactureStageDto> getStage(
            @PathVariable Long stageId) {
        return ResponseEntity.ok(stageService.getStageById(stageId));
    }

    @PutMapping("/user/update-stage/{stageId}")
    public ResponseEntity<ManufactureStageDto> updateStage(
            @PathVariable Long stageId, 
            @RequestBody ManuStageRequest stage) {
        return ResponseEntity.ok(stageService.updateStage(stageId, stage));
    }

    @DeleteMapping("/user/delete-stage/{stageId}")
    public ResponseEntity<String> deleteStage(
        @PathVariable Long stageId) {
        stageService.deleteStage(stageId);
        return  ResponseEntity.ok("Đã xoá công đoạn sản xuất thành công");
    }


}
