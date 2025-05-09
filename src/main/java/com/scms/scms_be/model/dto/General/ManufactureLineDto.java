package com.scms.scms_be.model.dto.General;

import lombok.Data;

@Data
public class ManufactureLineDto {
  private Long lineId;
  private Long companyId;
  private Long plantId;
  private String plantName;
  private String lineCode;
  private String lineName;
  private double capacity;
  private String description;
}
