package com.scms.scms_be.model.request.Delivery;

import lombok.Data;

@Data
public class DeliveryOrderRequest {
  private Long soId;
  private String status;

}