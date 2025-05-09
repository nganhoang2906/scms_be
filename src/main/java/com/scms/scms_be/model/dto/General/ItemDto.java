package com.scms.scms_be.model.dto.General;

import lombok.Data;

@Data
public class ItemDto {
  private Long itemId;
  private Long companyId;
  private String itemCode;
  private String itemName;
  private String itemType;
  private String uom;
  private String technicalSpecifications;
  private Double importPrice;
  private Double exportPrice;
  private String description;
}