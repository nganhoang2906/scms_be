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
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.request.EmployeeRequest;
import com.scms.scms_be.model.entity.General.Employee;
import com.scms.scms_be.service.General.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
   
    @Autowired
    private EmployeeService employeeService;

    //   Chỉ `C-ADMIN` mới có thể tạo nhân viên
    @PostMapping("/comad/create-employee")
    public ResponseEntity<Employee> createEmployee( @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeService.createEmployee( employeeRequest));
    }

    //   Lấy danh sách nhân viên trong công ty
    @GetMapping("/comad/all-employee-in-com/{companyId}")
    public ResponseEntity<List<Employee>> getAllEmployeeInCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(employeeService.getAllEmployeesInCompany(companyId));
    }

    //   Lấy nhân viên theo ID (chỉ xem trong công ty)
    @GetMapping("/user/get-employee/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById( employeeId));
    }

    //   Chỉnh sửa thông tin cá nhân (chỉ user đó có thể sửa)
    @PutMapping("/user/update-employee/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long employeeId,
            @RequestBody Employee updatedEmployee) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, updatedEmployee));
    }
    @DeleteMapping("/comad/delete-employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployeeById(id);
        if (deleted) {
            return ResponseEntity.ok("Employee and related user deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
  
}
