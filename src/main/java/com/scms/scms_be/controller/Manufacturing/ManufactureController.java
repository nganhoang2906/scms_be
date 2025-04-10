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

import com.scms.scms_be.model.dto.Manufacture.ManufactureOrderDto;
import com.scms.scms_be.model.dto.Manufacture.ManufactureProcessDto;
import com.scms.scms_be.model.dto.Manufacture.ManufactureStageDto;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureProcess;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;
import com.scms.scms_be.service.Manufacturing.ManufactureOrderService;
import com.scms.scms_be.service.Manufacturing.ManufactureProcessService;
import com.scms.scms_be.service.Manufacturing.ManufactureStageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManufactureController {

    @Autowired 
    private  ManufactureStageService stageService;
    @Autowired 
    private  ManufactureOrderService orderService;
    @Autowired 
    private  ManufactureProcessService processService;

    // --- ManufactureStage ---

    @PostMapping("/user/create-stage/{itemId}")
    public ResponseEntity<ManufactureStageDto> createStage(@PathVariable Long itemId ,@RequestBody ManufactureStage stage) {
        return ResponseEntity.ok(stageService.createStage(itemId,stage));
    }

    @GetMapping("/user/get-all-stage/{itemId}")
    public ResponseEntity<List<ManufactureStageDto>> getAllStagesByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(stageService.getAllStagesByItemId(itemId));
    }

    @GetMapping("/user/get-stage/{stageId}")
    public ResponseEntity<ManufactureStageDto> getStage(@PathVariable Long stageId) {
        return ResponseEntity.ok(stageService.getStageById(stageId));
    }

    @PutMapping("/user/update-stage/{stageId}")
    public ResponseEntity<ManufactureStageDto> updateStage(@PathVariable Long stageId, @RequestBody ManufactureStage stage) {
        return ResponseEntity.ok(stageService.updateStage(stageId, stage));
    }

    @DeleteMapping("/user/delete-stage/{stageId}")
    public ResponseEntity<String> deleteStage(@PathVariable Long stageId) {
        stageService.deleteStage(stageId);
        return  ResponseEntity.ok("Đã xoá Stage thành công");
    }

    // --- ManufactureOrder ---

    @PostMapping("/user/create-order/{itemId}/{lineId}")
    public ResponseEntity<ManufactureOrderDto> createOrder(@PathVariable Long itemId, @PathVariable Long lineId, @RequestBody ManufactureOrder order) {
        return ResponseEntity.ok(orderService.createOrder( itemId, lineId,order));
    }

    @GetMapping("/user/get-all-order-in-item/{itemId}")
    public ResponseEntity<List<ManufactureOrderDto>> getAllOrders(@PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.getAllManufactureOrdersbyItemId(itemId));
    }

    @GetMapping("/user/get-all-order-in-com/{companyId}")
    public ResponseEntity<List<ManufactureOrderDto>> getAllOrdersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(orderService.getAllManufactureOrdersByCompanyId(companyId));
    }

    @GetMapping("/user/get-order/{moid}")
    public ResponseEntity<ManufactureOrderDto> getOrder(@PathVariable Long moid) {
        return ResponseEntity.ok(orderService.getById(moid));
    }

    @PutMapping("/user/update-order/{moid}")
    public ResponseEntity<ManufactureOrderDto> updateOrder(@PathVariable Long moid, @RequestBody ManufactureOrder order) {
        return ResponseEntity.ok(orderService.update(moid, order));
    }

    // --- ManufactureProcess ---

    @PostMapping("/user/create-process/{moId}/{stageId}")
    public ResponseEntity<ManufactureProcessDto> createProcess(@PathVariable Long moId, @PathVariable Long stageId, @RequestBody ManufactureProcess process) {
        return ResponseEntity.ok(processService.create( moId, stageId,process));
    }

    @GetMapping("/user/get-all-process-in-com/{companyId}")
    public ResponseEntity<List<ManufactureProcessDto>> getAllProcesses(@PathVariable Long companyId) {
        return ResponseEntity.ok(processService.getAllByCompany(companyId));
    }

    @GetMapping("/user/get-process/{mpid}")
    public ResponseEntity<ManufactureProcessDto> getProcess(@PathVariable Long mpid) {
        return ResponseEntity.ok(processService.getById(mpid));
    }

    @PutMapping("/user/update-process/{mpid}")
    public ResponseEntity<ManufactureProcessDto> updateProcess(@PathVariable Long mpid, @RequestBody ManufactureProcess process) {
        return ResponseEntity.ok(processService.update(mpid, process));
    }
}
