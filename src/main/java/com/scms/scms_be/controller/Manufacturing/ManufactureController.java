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
    public ResponseEntity<ManufactureOrder> createOrder(@PathVariable Long itemId, @PathVariable Long lineId, @RequestBody ManufactureOrder order) {
        return ResponseEntity.ok(orderService.createOrder(order, itemId, lineId));
    }

    @GetMapping("/user/get-all-order-in-item/{itemId}")
    public ResponseEntity<List<ManufactureOrder>> getAllOrders(@PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.getAllManufactureOrdersbyItemId(itemId));
    }

    @GetMapping("/user/get-all-order-in-com/{companyId}")
    public ResponseEntity<List<ManufactureOrder>> getAllOrdersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(orderService.getAllManufactureOrdersByCompanyId(companyId));
    }

    @GetMapping("/user/get-order/{id}")
    public ResponseEntity<ManufactureOrder> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PutMapping("/user/update-order/{id}")
    public ResponseEntity<ManufactureOrder> updateOrder(@PathVariable Long id, @RequestBody ManufactureOrder order) {
        return ResponseEntity.ok(orderService.update(id, order));
    }

    // --- ManufactureProcess ---

    @PostMapping("/user/create-process/{moId}/{stageId}")
    public ResponseEntity<ManufactureProcess> createProcess(@PathVariable Long moId, @PathVariable Long stageId, @RequestBody ManufactureProcess process) {
        return ResponseEntity.ok(processService.create(process, moId, stageId));
    }

    @GetMapping("/user/get-all-process/{companyId}")
    public ResponseEntity<List<ManufactureProcess>> getAllProcesses(@PathVariable Long companyId) {
        return ResponseEntity.ok(processService.getAllByCompany(companyId));
    }

    @GetMapping("/user/get-process/{id}")
    public ResponseEntity<ManufactureProcess> getProcess(@PathVariable Long id) {
        return ResponseEntity.ok(processService.getById(id));
    }

    @PutMapping("/user/update-process/{id}")
    public ResponseEntity<ManufactureProcess> updateProcess(@PathVariable Long id, @RequestBody ManufactureProcess process) {
        return ResponseEntity.ok(processService.update(id, process));
    }
}
