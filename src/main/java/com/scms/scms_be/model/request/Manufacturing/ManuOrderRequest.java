package com.scms.scms_be.model.request.Manufacturing;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ManuOrderRequest {
  private Long itemId;
  private Long lineId;
  private String type;
  private Double quantity;
  private LocalDateTime estimatedStartTime;
  private LocalDateTime estimatedEndTime;
  private String createdBy;
  private String status;
}
