package com.scms.scms_be.model.request.Auth;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
  private String currentPassword;
  private String newPassword;
}
