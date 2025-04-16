package com.scms.scms_be.model.request.General;

import lombok.Data;

@Data
public class UpdateInfoRequest {
    private String username;

    private String email;

    private String status;
}
