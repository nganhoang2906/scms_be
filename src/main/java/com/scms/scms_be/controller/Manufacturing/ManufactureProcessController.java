package com.scms.scms_be.controller.Manufacturing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.Manufacture.ManufactureProcessDto;
import com.scms.scms_be.model.request.Manufacturing.ManuProcessRequest;
import com.scms.scms_be.service.Manufacturing.ManufactureProcessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManufactureProcessController {
    // --- ManufactureProcess ---
    @Autowired
    private final ManufactureProcessService processService;
    
    @PostMapping("/user/create-process/{moId}/{stageId}")
    public ResponseEntity<ManufactureProcessDto> createProcess(
        @PathVariable Long moId, 
        @PathVariable Long stageId, 
        @RequestBody ManuProcessRequest process) {
        return ResponseEntity.ok(processService.create( moId, stageId,process));
    }

    @GetMapping("/user/get-all-process-in-com/{companyId}")
    public ResponseEntity<List<ManufactureProcessDto>> getAllProcesses(
        @PathVariable Long moId) {
        return ResponseEntity.ok(processService.getAllByMoId(moId));
    }

    @GetMapping("/user/get-process/{mpid}")
    public ResponseEntity<ManufactureProcessDto> getProcess(
        @PathVariable Long mpid) {
        return ResponseEntity.ok(processService.getById(mpid));
    }

    @PutMapping("/user/update-process/{mpid}")
    public ResponseEntity<ManufactureProcessDto> updateProcess(
        @PathVariable Long mpid, 
        @RequestBody ManuProcessRequest process) {
        return ResponseEntity.ok(processService.update(mpid, process));
    }
}
