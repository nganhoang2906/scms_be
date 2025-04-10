package com.scms.scms_be.model.dto.Manufacture;

import java.util.Date;

import lombok.Data;

@Data
public class ManufactureProcessDto {
    private Long id;

    private Long moId;
    
    private Long stageId;

    private Date startedOn;
    private Date finishedOn;
    private String status;
    
}
