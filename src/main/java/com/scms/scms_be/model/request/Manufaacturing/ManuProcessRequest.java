package com.scms.scms_be.model.request.Manufaacturing;

import java.util.Date;

import lombok.Data;

@Data
public class ManuProcessRequest {
    private Date startedOn;
    private Date finishedOn;
    private String status;
}
