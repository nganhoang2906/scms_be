package com.scms.scms_be.model.request.Purchasing;

import lombok.Data;

@Data
public class PurchaseOrderDetailRequest {
  private Long itemId;
  private Double quantity;
  private Double itemPrice;
  private String note;
}
