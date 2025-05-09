package com.scms.scms_be.controller.Sales;

import com.scms.scms_be.model.dto.Sales.SalesOrderDto;
import com.scms.scms_be.model.request.Sales.SalesOrderRequest;
import com.scms.scms_be.service.Sales.SalesOrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SalesOrderController {

  @Autowired
  private SalesOrderService salesOrderService;

  @PostMapping("/user/create-sales-order")
  public ResponseEntity<SalesOrderDto> createSalesOrder(@RequestBody SalesOrderRequest request) {
    return ResponseEntity.ok(salesOrderService.createSalesOrder(request));
  }

  @GetMapping("/user/get-sales-order/{soId}")
  public ResponseEntity<SalesOrderDto> getSalesOrderById(@PathVariable Long soId) {
    return ResponseEntity.ok(salesOrderService.getSalesOrderById(soId));
  }

  @GetMapping("/user/get-all-sales-order-in-com/{companyId}")
  public ResponseEntity<List<SalesOrderDto>> getAllSalesOrdersByCompany(@PathVariable Long companyId) {
    return ResponseEntity.ok(salesOrderService.getAllSalesOrdersByCompany(companyId));
  }

  @GetMapping("/user/get-all-sales-order-by-po/{poId}")
  public ResponseEntity<List<SalesOrderDto>> getAllSalesOrderByPoId(@PathVariable Long poId) {
    return ResponseEntity.ok(salesOrderService.getAllSalesOrderByPoId(poId));
  }

  @PutMapping("/user/update-sales-order-status/{soId}")
  public ResponseEntity<SalesOrderDto> updateStatus(
      @PathVariable Long soId,
      @RequestParam String status) {
    return ResponseEntity.ok(salesOrderService.updateSoStatus(soId, status));
  }
  
}
