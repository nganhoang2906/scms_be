package com.scms.scms_be.model.dto.Manufacture;

import lombok.Data;

@Data
public class ManufactureStageDetailDto {
  private Long stageDetailId;
  private Long stageId;
  private String stageName;
  private Integer stageOrder;
  private Long estimatedTime;
  private String description;
}
