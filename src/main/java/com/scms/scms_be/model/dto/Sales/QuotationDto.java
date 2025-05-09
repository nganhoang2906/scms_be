package com.scms.scms_be.model.dto.Sales;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class QuotationDto {
  private Long quotationId;
  private String quotationCode;
  private Long companyId;
  private String companyName;
  private Long rfqId;
  private String rfqCode;
  private Double totalPrice;
  private LocalDate availableByDate;
  private String createdBy;
  private LocalDateTime createdOn;
  private LocalDateTime lastUpdatedOn;
  private String status;

  private List<QuotationDetailDto> quotationDetails;
}
