package com.scms.scms_be.model.request.General;

import lombok.Data;

@Data
public class ManuLineRequest {
    private String lineCode;
    private String lineName;

    private double capacity;

    private String description;
}
