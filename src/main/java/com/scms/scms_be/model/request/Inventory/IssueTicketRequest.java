package com.scms.scms_be.model.request.Inventory;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class IssueTicketRequest {
  private Long companyId;
  private Long warehouseId;
  private LocalDateTime issueDate;
  private String reason;
  private String issueType;
  private String referenceCode;
  private String createdBy;
  private String status;
  private String file;
  private String note;
}
