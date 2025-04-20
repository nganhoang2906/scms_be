package com.scms.scms_be.model.dto.General;


import lombok.Data;

@Data
public class ManufacturePlantDto {
    private Long plantId;
    private Long companyId;
    private String plantCode;
    private String plantName;
    private String description;

}
