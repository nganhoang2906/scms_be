package com.scms.scms_be.model.dto.Delivery;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DeliveryOrderDto {
  private Long doId;
  private String doCode;
  private Long soId;
  private String soCode;
  private String createBy;
  private LocalDateTime createdOn;
  private LocalDateTime lastUpdatedOn;
  private String status;
}
