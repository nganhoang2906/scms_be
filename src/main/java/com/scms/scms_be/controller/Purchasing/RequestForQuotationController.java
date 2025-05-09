package com.scms.scms_be.controller.Purchasing;

import com.scms.scms_be.model.dto.Purchasing.RequestForQuotationDto;
import com.scms.scms_be.model.request.Purchasing.RequestForQuotationRequest;
import com.scms.scms_be.service.Purchasing.RequestForQuotationService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestForQuotationController {

  @Autowired
  private RequestForQuotationService rfqService;

  @PostMapping("/user/create-rfq")
  public ResponseEntity<RequestForQuotationDto> createRFQ(
      @RequestBody RequestForQuotationRequest request) {
    return ResponseEntity.ok(rfqService.createRFQ(request));
  }

  @GetMapping("/user/get-all-rfq-in-com/{companyId}")
  public ResponseEntity<List<RequestForQuotationDto>> getAllByCompany(
      @PathVariable Long companyId) {
    return ResponseEntity.ok(rfqService.getAllByCompany(companyId));
  }

  @GetMapping("/user/get-all-rfq-by-requested-company/{requestedCompanyId}")
  public ResponseEntity<List<RequestForQuotationDto>> getAllByRequestedCompany(
      @PathVariable Long requestedCompanyId) {
    return ResponseEntity.ok(rfqService.getAllByRequestCompany(requestedCompanyId));
  }

  @GetMapping("/user/get-rfq-by-id/{rfqId}")
  public ResponseEntity<RequestForQuotationDto> getById(@PathVariable Long rfqId) {
    return ResponseEntity.ok(rfqService.getById(rfqId));
  }

  @PutMapping("/user/update-rfq-status/{rfqId}")
  public ResponseEntity<RequestForQuotationDto> updateStatus(
      @PathVariable Long rfqId,
      @RequestParam String status) {
    return ResponseEntity.ok(rfqService.updateStatus(rfqId, status));
  }
  
}
