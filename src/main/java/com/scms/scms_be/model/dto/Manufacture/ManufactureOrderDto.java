package com.scms.scms_be.model.dto.Manufacture;

import java.util.Date;

import lombok.Data;

@Data
public class ManufactureOrderDto {
    
    private Long moId;

    private Long itemId;
    private Long lineId;
    
    private String moCode;

    private String batchNo;
    private String type;
    private Integer quantity;

    private Date estimatedStartTime;
    private Date estimatedEndTime;

    private String createdBy;
    private Date createdOn;
    private Date lastUpdatedOn;
    private String status;
}
