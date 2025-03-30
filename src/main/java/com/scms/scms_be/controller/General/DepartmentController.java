package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.service.General.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    
      //   Thêm phòng ban 
    // @PostMapping("/sysad/create-department")
    // public ResponseEntity<Department> createDepartment( @RequestBody DepartmentRequest departmentRequest) {
    //     return ResponseEntity.ok(departmentService.createDepartment( departmentRequest));
    // }

    //   Lấy danh sách phòng ban trong công ty 
    @GetMapping("/comad/all-department-in-com/{companyId}")
    public ResponseEntity<List<Department>> getAllDepartment(@PathVariable Long companyId) {
        return ResponseEntity.ok(departmentService.getAllDepartmentInCompany(companyId));
    }

    @GetMapping("/comad/get-department/{departmentId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentById( departmentId));
    }

    //   Cập nhật phòng ban 
    @PutMapping("/comad/update-department/{departmentId}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody Department updatedDept) {
        return ResponseEntity.ok(departmentService.updateDepartment( departmentId, updatedDept));
    }

    @DeleteMapping("/comad/delete-department/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        boolean deleted = departmentService.deleteDepartmentById(id);
        if (deleted) {
            return ResponseEntity.ok("Department and related employees/users deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
   
}
