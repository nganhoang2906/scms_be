package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.General.DepartmentDto;
import com.scms.scms_be.service.General.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Lấy danh sách phòng ban trong công ty
    @GetMapping("/user/get-all-department-in-company/{companyId}")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments(@PathVariable Long companyId) {
        List<DepartmentDto> departments = departmentService.getAllDepartmentInCompany(companyId);
        return ResponseEntity.ok(departments);
    }

    // Lấy thông tin chi tiết của phòng ban
    @GetMapping("/user/get-department/{departmentId}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long departmentId) {
        DepartmentDto department = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(department);
    }


   
}
