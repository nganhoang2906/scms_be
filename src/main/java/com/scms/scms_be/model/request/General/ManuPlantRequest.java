package com.scms.scms_be.model.request.General;

import lombok.Data;

@Data
public class ManuPlantRequest {
    private String plantCode;
    private String plantName;
    private String description;
}
