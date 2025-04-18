package com.scms.scms_be.model.dto.Manufacture;

import java.util.Date;

import lombok.Data;

@Data
public class ManufactureOrderDto {
    
    private Long moId;
    private String moCode;

    private Long itemId;
    private String itemCode;
    private String itemName;

    private Long lineId;
    private String lineCode;
    private String lineName;
    
    private String type;
    private Integer quantity;

    private Date estimatedStartTime;
    private Date estimatedEndTime;

    private String createdBy;
    private Date createdOn;
    private Date lastUpdatedOn;
    private String status;
}
