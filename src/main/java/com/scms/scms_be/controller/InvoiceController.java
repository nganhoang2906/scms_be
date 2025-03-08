package com.scms.scms_be.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.entity.Invoice;
import com.scms.scms_be.service.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    @Autowired
    private final InvoiceService invoiceService;

    // Gọi service để tạo hóa đơn
    @PostMapping("/auth/invoice/generate-and-save")
    public ResponseEntity<String> generateAndSaveInvoice(@RequestBody Map<String, Object> invoiceData) {
        return invoiceService.generateAndSaveInvoice(invoiceData);
    }
    
      // API để lấy tất cả hóa đơn
    @GetMapping("/auth/get-all-invoice")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
}