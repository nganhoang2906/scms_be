package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.request.EmployeeRequest;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.model.entity.General.Employee;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.repository.General.DepartmentRepository;
import com.scms.scms_be.repository.General.EmployeeRepository;
import com.scms.scms_be.repository.General.UserRepository;

@Service
public class EmployeeService {

   @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DepartmentRepository departmentRepo;

    // Tạo tài khoản nhân viên + User
    public Employee createEmployee(EmployeeRequest request) {
    
         if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email đã được sử dụng trong User!", HttpStatus.BAD_REQUEST);
        }
        
        if (employeeRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email đã được sử dụng trong Employee!", HttpStatus.BAD_REQUEST);
        }
        // Kiểm tra `employeeCode`
        if (employeeRepo.findByEmployeeCode(request.getEmployeeCode()).isPresent()) {
            throw new CustomException("Mã nhân viên đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra `identityNumber`
        if (employeeRepo.findByIdentityNumber(request.getIdentityNumber()).isPresent()) {
            throw new CustomException("Số CCCD/CMND đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra `username` trong `User`
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new CustomException("Tên đăng nhập đã được sử dụng!", HttpStatus.BAD_REQUEST);
        }

        
          Department department = departmentRepo.findById(request.getDepartmentId())
                .orElseThrow(() -> new CustomException("Phòng ban không tồn tại!", HttpStatus.NOT_FOUND));

            // Tạo Employee
            Employee employee = new Employee();
            employee.setEmployeeCode(request.getEmployeeCode());
            employee.setEmployeeName(request.getEmployeeName());
            employee.setPosition(request.getPosition());
            employee.setGender(request.getGender());
            employee.setIdentityNumber(request.getIdentityNumber());
            employee.setAddress(request.getAddress());
            employee.setEmail(request.getEmail());
            employee.setPhoneNumber(request.getPhoneNumber());
            employee.setDateOfBirth(request.getDateOfBirth());
            employee.setEmploymentStartDate(request.getEmploymentStartDate());
            employee.setStatus(request.getStatus());
            employee.setDepartment(department);

            Employee savedEmployee = employeeRepo.save(employee);

            // Tạo User liên kết với Employee
            User newUser = new User(
                    savedEmployee,
                    request.getEmail(),
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword()), // Mã hóa password
                    "USER",
                    null, // OTP không cần thiết
                    "Active",
                    true
            );

            userRepo.save(newUser);

            return savedEmployee;
    }

    // Lấy danh sách tất cả nhân viên
    public List<Employee> getAllEmployeesInCompany(Long companyId) {
        return employeeRepo.findByDepartmentCompanyCompanyId(companyId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    // Lấy thông tin nhân viên theo ID
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepo.findById(employeeId)
                .orElseThrow(() -> new CustomException("Nhân viên không tồn tại!", HttpStatus.NOT_FOUND));
    }

    // Cập nhật thông tin nhân viên (bất kỳ ai cũng có thể cập nhật)
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new CustomException("Nhân viên không tồn tại!", HttpStatus.NOT_FOUND));

        // Gán trực tiếp giá trị từ `updatedEmployee`
        existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
        existingEmployee.setPosition(updatedEmployee.getPosition());
        existingEmployee.setGender(updatedEmployee.getGender());
        existingEmployee.setIdentityNumber(updatedEmployee.getIdentityNumber());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existingEmployee.setDateOfBirth(updatedEmployee.getDateOfBirth());
        existingEmployee.setEmploymentStartDate(updatedEmployee.getEmploymentStartDate());
        existingEmployee.setAvatar(updatedEmployee.getAvatar());
        existingEmployee.setStatus(updatedEmployee.getStatus());

        return employeeRepo.save(existingEmployee);
    }

   
}
