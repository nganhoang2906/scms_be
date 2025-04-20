package com.scms.scms_be.model.request.Manufacturing;

import lombok.Data;

@Data
public class ManuStageDetailRequest {

    private String stageName;
    private Integer stageOrder;
    private Long estimatedTime; // minutes
    private String description;
}
