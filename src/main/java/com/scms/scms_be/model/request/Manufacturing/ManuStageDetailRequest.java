package com.scms.scms_be.model.request.Manufacturing;

import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ManuStageDetailRequest {

    private String stageName;
    private Integer stageOrder;
    private Long estimatedTime; // minutes
    private String description;
}
