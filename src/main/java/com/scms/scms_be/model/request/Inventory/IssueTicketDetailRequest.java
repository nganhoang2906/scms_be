package com.scms.scms_be.model.request.Inventory;

import lombok.Data;

@Data
public class IssueTicketDetailRequest {
  private Long itemId;
  private Double quantity;
  private String note;
}
