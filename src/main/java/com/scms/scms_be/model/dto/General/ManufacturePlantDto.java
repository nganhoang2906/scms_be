package com.scms.scms_be.model.dto.General;

import java.util.List;

import lombok.Data;

@Data
public class ManufacturePlantDto {
    private Long plantId;
    private Long companyId;
    private String plantCode;
    private String plantName;
    private String description;

    private List<ManufacturePlantDto> PlantList;
}
