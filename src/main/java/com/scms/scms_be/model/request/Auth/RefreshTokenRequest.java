package com.scms.scms_be.model.request.Auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String token;
}
