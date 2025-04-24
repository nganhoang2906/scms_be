package com.scms.scms_be.controller.Sales;

import com.scms.scms_be.model.dto.Sales.InvoiceDto;
import com.scms.scms_be.model.request.Sales.InvoiceRequest;
import com.scms.scms_be.service.Sales.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // Tạo mới hóa đơn từ đơn hàng bán
    @PostMapping("/user/create-invoice")
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceRequest request) {
        InvoiceDto invoiceDto = invoiceService.generateInvoice(request);
        return ResponseEntity.ok(invoiceDto);
    }

    // (Tuỳ chọn) Lấy thông tin hóa đơn theo ID
    // @GetMapping("/user/get-invoice/{invoiceId}")
    // public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long invoiceId) {
    //     InvoiceDto invoiceDto = invoiceService.getInvoiceById(invoiceId);
    //     return ResponseEntity.ok(invoiceDto);
    // }

    // // (Tuỳ chọn) Lấy tất cả hóa đơn của công ty bán
    // @GetMapping("/user/get-all-invoice-by-company/{companyId}")
    // public ResponseEntity<?> getAllInvoiceByCompany(@PathVariable Long companyId) {
    //     return ResponseEntity.ok(invoiceService.getAllInvoiceByCompany(companyId));
    // }
}
