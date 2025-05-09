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

import com.scms.scms_be.model.dto.Manufacture.ManufactureOrderDto;
import com.scms.scms_be.model.request.Manufacturing.ManuOrderRequest;
import com.scms.scms_be.service.Manufacturing.ManufactureOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManufactureOrderController {

  @Autowired
  private final ManufactureOrderService orderService;

  @PostMapping("/user/create-mo")
  public ResponseEntity<ManufactureOrderDto> createOrder(
      @RequestBody ManuOrderRequest request) {
    return ResponseEntity.ok(orderService.createOrder(request));
  }

  @GetMapping("/user/get-all-mo-in-item/{itemId}")
  public ResponseEntity<List<ManufactureOrderDto>> getAllOrders(
      @PathVariable Long itemId) {
    return ResponseEntity.ok(orderService.getAllManufactureOrdersbyItemId(itemId));
  }

  @GetMapping("/user/get-all-mo-in-com/{companyId}")
  public ResponseEntity<List<ManufactureOrderDto>> getAllOrdersByCompany(
      @PathVariable Long companyId) {
    return ResponseEntity.ok(orderService.getAllManufactureOrdersByCompanyId(companyId));
  }

  @GetMapping("/user/get-mo/{moid}")
  public ResponseEntity<ManufactureOrderDto> getOrder(
      @PathVariable Long moid) {
    return ResponseEntity.ok(orderService.getById(moid));
  }

  @PutMapping("/user/update-mo/{moid}")
  public ResponseEntity<ManufactureOrderDto> updateOrder(
      @PathVariable Long moid,
      @RequestBody ManuOrderRequest order) {
    return ResponseEntity.ok(orderService.update(moid, order));
  }

}
