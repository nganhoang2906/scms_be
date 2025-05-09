package com.scms.scms_be.controller.General;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.EmployeeDto;
import com.scms.scms_be.model.request.General.EmployeeRequest;
import com.scms.scms_be.service.General.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @PostMapping("/comad/create-employee")
  public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
    return ResponseEntity.ok(employeeService.createEmployee(employeeRequest));
  }

  @GetMapping("/user/get-all-employee-in-com/{companyId}")
  public ResponseEntity<List<EmployeeDto>> getAllEmployeeInCompany(@PathVariable Long companyId) {
    return ResponseEntity.ok(employeeService.getAllEmployeesInCompany(companyId));
  }

  @GetMapping("/user/get-employee/{employeeId}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long employeeId) {
    return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
  }

  @PutMapping("/user/update-employee/{employeeId}")
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long employeeId,
      @RequestBody EmployeeRequest updatedEmployee) {
    return ResponseEntity.ok(employeeService.updateEmployee(employeeId, updatedEmployee));
  }

  @DeleteMapping("/comad/delete-employee/{employeeId}")
  public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
    boolean deleted = employeeService.deleteEmployeeById(employeeId);
    if (deleted) {
      return ResponseEntity.ok("Nhân viên và tài khoản liên quan đã được xóa thành công");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/user/update-avatar/{employeeId}")
  public ResponseEntity<String> updateEmployeeAvatar(
      @PathVariable Long employeeId,
      @RequestParam("avatar") MultipartFile avatarFile) {

    try {
      String avatarUrl = employeeService.updateEmployeeAvatar(employeeId, avatarFile);
      return ResponseEntity.ok(avatarUrl);
    } catch (IOException e) {
      throw new CustomException("Cập nhật ảnh đại diện thất bại!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
