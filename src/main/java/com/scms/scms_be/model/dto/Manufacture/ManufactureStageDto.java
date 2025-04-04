package com.scms.scms_be.model.dto.Manufacture;

import lombok.Data;

@Data
public class ManufactureStageDto {
    private Long stageId;
    private Long itemId;
    private String stageName;
    private Integer stageOrder;
    private Long estimatedTime;
    private String description;
}