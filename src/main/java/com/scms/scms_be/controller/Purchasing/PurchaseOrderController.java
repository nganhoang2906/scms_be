package com.scms.scms_be.controller.Purchasing;

import com.scms.scms_be.model.dto.Purchasing.PurchaseOrderDto;
import com.scms.scms_be.model.request.Purchasing.PurchaseOrderRequest;
import com.scms.scms_be.service.Purchasing.PurchaseOrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/user/create-po")
    public ResponseEntity<PurchaseOrderDto> createPurchaseOrder(
            @RequestBody PurchaseOrderRequest request) {
        PurchaseOrderDto created = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/get-all-po-in-com/{companyId}")
    public ResponseEntity<List<PurchaseOrderDto>> getAllPoInCompany(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(purchaseOrderService.getAllPoByCompany(companyId));
    }
    @GetMapping("/user/get-all-po-by-supplier/{supplierCompanyId}")
    public ResponseEntity<List<PurchaseOrderDto>> getAllPoBySupplier(
            @PathVariable Long supplierCompanyId) {
        return ResponseEntity.ok(purchaseOrderService.getAllPoBySupplierCompany(supplierCompanyId));
    }

    @GetMapping("/user/get-po/{poId}")
    public ResponseEntity<PurchaseOrderDto> getPoById(@PathVariable Long poId) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(poId));
    }

    @PutMapping("/user/update-po-status/{poId}")
    public ResponseEntity<PurchaseOrderDto> updateStatus(
            @PathVariable Long poId,
            @RequestParam String status) {
        return ResponseEntity.ok(purchaseOrderService.updatePoStatus(poId, status));
    }
}
