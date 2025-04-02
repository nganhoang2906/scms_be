package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.scms.scms_be.model.dto.DepartmentDto;
import com.scms.scms_be.model.dto.request.DepartmentRequest;
import com.scms.scms_be.service.General.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Thêm phòng ban
    @PostMapping("/sysad/create")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentRequest departmentRequest) {
        DepartmentDto createdDepartment = departmentService.createDepartment(departmentRequest);
        return ResponseEntity.ok(createdDepartment);
    }

    // Lấy danh sách phòng ban trong công ty
    @GetMapping("/comad/all-department-in-company/{companyId}")
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

    // Cập nhật phòng ban
    @PutMapping("/comad/update-department/{departmentId}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody DepartmentRequest updatedDept) {
        DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentId, updatedDept);
        return ResponseEntity.ok(updatedDepartment);
    }

    // Xóa phòng ban
    @DeleteMapping("/comad/delete/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        boolean deleted = departmentService.deleteDepartmentById(id);
        if (deleted) {
            return ResponseEntity.ok("Phòng ban và nhân viên liên quan đã được xóa thành công.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
