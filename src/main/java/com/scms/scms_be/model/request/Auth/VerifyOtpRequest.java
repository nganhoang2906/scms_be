package com.scms.scms_be.model.request.Auth;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    @Email
    private String email;
    
    private String otp;
}
