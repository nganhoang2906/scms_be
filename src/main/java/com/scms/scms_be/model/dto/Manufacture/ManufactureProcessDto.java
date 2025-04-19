package com.scms.scms_be.model.dto.Manufacture;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

@Data
public class ManufactureProcessDto {
    private Long id;

    private Long moId;
    private String moCode;
    
    private Long stageId;
    private String stageName;
    private Integer stageOrder;

    private LocalDateTime startedOn;
    private LocalDateTime finishedOn;
    private String status;
    
}
