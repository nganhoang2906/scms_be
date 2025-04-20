package com.scms.scms_be.model.dto.Manufacture;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ManufactureProcessDto {
    private Long id;

    private Long moId;
    private String moCode;
    
    private Long stageDetailId;
    private String stageDetailName;
    private Integer stageDetailOrder;

    private LocalDateTime startedOn;
    private LocalDateTime finishedOn;
    private String status;
    
}
