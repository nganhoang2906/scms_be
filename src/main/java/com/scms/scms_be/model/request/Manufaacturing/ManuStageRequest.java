package com.scms.scms_be.model.request.Manufaacturing;

import lombok.Data;

@Data
public class ManuStageRequest {
    private String stageName;
    private Integer stageOrder;
    private Long estimatedTime;
    private String description;
}
