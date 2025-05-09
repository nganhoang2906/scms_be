package com.scms.scms_be.model.dto.Sales;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class InvoiceDto {
  private Long invoiceId;
  private String invoiceCode;
  private Long salesCompanyId;
  private String salesCompanyName;
  private Long purchaseCompanyId;
  private String purchaseCompanyName;
  private Long soId;
  private String soCode;
  private Double subTotal;
  private Double taxRate;
  private Double taxAmount;
  private Double totalAmount;
  private String paymentMethod;
  private String createBy;
  private LocalDateTime createdOn;
  private String status;
  private String file;
}
