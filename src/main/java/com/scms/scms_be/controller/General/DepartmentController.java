package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.request.DepartmentRequest;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.service.General.DepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    
      //   Thêm phòng ban 
    @PostMapping("/sysad/create-department")
    public ResponseEntity<Department> createDepartment( @RequestBody DepartmentRequest departmentRequest) {
        return ResponseEntity.ok(departmentService.createDepartment( departmentRequest));
    }

    //   Lấy danh sách phòng ban trong công ty 
    @GetMapping("/comad/all-department-in-company")
    public ResponseEntity<List<Department>> getAllDepartment(@RequestParam Long companyId) {
        return ResponseEntity.ok(departmentService.getAllDepartmentInCompany(companyId));
    }

    @GetMapping("/sysad/all-department")
    public ResponseEntity<List<Department>> getAllDepartment() {
        return ResponseEntity.ok(departmentService.getAllDepartment());
    }

    //   Cập nhật phòng ban 
    @PutMapping("/comad/update-department/{departmentId}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable Long departmentId,
            @RequestBody Department updatedDept) {
        return ResponseEntity.ok(departmentService.updateDepartment( departmentId, updatedDept));
    }

    //  Xóa phòng ban (chỉ S-Admin)
    @DeleteMapping("/sysad/delete-department/{departmentId}")
    public ResponseEntity<String> deleteDepartment( @PathVariable Long departmentId  ) {
        departmentService.deleteDepartment( departmentId);
        return ResponseEntity.ok("Phòng ban đã được xóa!");
    }
}
