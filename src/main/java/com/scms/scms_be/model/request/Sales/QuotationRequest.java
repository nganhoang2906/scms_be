package com.scms.scms_be.model.request.Sales;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class QuotationRequest {
  private Long companyId;
  private Long rfqId;
  private LocalDate availableByDate;
  private String createdBy;
  private String status;

  private List<QuotationDetailRequest> quotationDetails;

}
