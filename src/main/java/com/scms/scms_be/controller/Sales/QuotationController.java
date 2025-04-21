package com.scms.scms_be.controller.Sales;

import com.scms.scms_be.model.dto.Sales.QuotationDto;
import com.scms.scms_be.model.request.Sales.QuotationRequest;
import com.scms.scms_be.service.Sales.QuotationService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @PostMapping("/user/create-quotation")
    public ResponseEntity<QuotationDto> createQuotation(@RequestBody QuotationRequest request) {
        return ResponseEntity.ok(quotationService.createQuotation(request));
    }

    @GetMapping("/user/get-quotation-by-id/{quotationId}")
    public ResponseEntity<QuotationDto> getQuotationById(@PathVariable Long quotationId) {
        return ResponseEntity.ok(quotationService.getQuotationById(quotationId));
    }

    @GetMapping("/user/get-all-quotations-by-company/{companyId}")
    public ResponseEntity<List<QuotationDto>> getAllByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(quotationService.getAllQuotationsByCompany(companyId));
    }

    @GetMapping("/user/get-all-quotations-by-rfq/{rfqId}")
    public ResponseEntity<List<QuotationDto>> getAllByRfq(@PathVariable Long rfqId) {
        return ResponseEntity.ok(quotationService.getAllQuotationsByRfq(rfqId));
    }
}