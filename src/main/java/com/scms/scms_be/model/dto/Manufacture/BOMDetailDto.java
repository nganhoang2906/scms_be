package com.scms.scms_be.model.dto.Manufacture;

import lombok.Data;

@Data
public class BOMDetailDto {
  private Long id;
  private Long bomId;
  private Long itemId;
  private String itemCode;
  private String itemName;
  private Double quantity;
  private String note;
}
