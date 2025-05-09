package com.scms.scms_be.service.General;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.EmployeeDto;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.model.entity.General.Employee;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.model.request.General.EmployeeRequest;
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

  @Autowired
  private AwsS3Service awsS3Service;

  public EmployeeDto createEmployee(EmployeeRequest request) {

    if (userRepo.findByEmail(request.getEmail()).isPresent()) {
      throw new CustomException("Email đã được sử dụng!", HttpStatus.BAD_REQUEST);
    }

    if (employeeRepo.findByEmail(request.getEmail()).isPresent()) {
      throw new CustomException("Email đã được sử dụng!", HttpStatus.BAD_REQUEST);
    }
    if (employeeRepo.findByEmployeeCode(request.getEmployeeCode()).isPresent()) {
      throw new CustomException("Mã nhân viên đã được sử dụng!", HttpStatus.BAD_REQUEST);
    }

    if (userRepo.findByUsername(request.getUsername()).isPresent()) {
      throw new CustomException("Tên đăng nhập đã được sử dụng!", HttpStatus.BAD_REQUEST);
    }

    Department department = departmentRepo.findById(request.getDepartmentId())
        .orElseThrow(() -> new CustomException("Không tìm thấy bộ phận!", HttpStatus.NOT_FOUND));

    Employee employee = new Employee();
    employee.setEmployeeCode(request.getEmployeeCode());
    employee.setEmployeeName(request.getEmployeeName());
    employee.setPosition(request.getPosition());
    employee.setGender(request.getGender());
    employee.setAddress(request.getAddress());
    employee.setEmail(request.getEmail());
    employee.setPhoneNumber(request.getPhoneNumber());
    employee.setDateOfBirth(request.getDateOfBirth());
    employee.setStatus("Đang làm việc");
    employee.setDepartment(department);

    Employee savedEmployee = employeeRepo.save(employee);

    String departmentName = department.getDepartmentName();
    String role;

    if ("Quản trị".equals(departmentName)) {
      role = "C-ADMIN";
    } else {
      role = "USER";
    }

    User newUser = new User(
        savedEmployee,
        request.getEmail(),
        generateUsername(request.getEmail(), department.getCompany().getCompanyId()),
        passwordEncoder.encode(request.getPassword()),
        role,
        null,
        "Đang hoạt động",
        true);
    userRepo.save(newUser);

    return convertToDto(savedEmployee);
  }

  public String generateUsername(String email, Long companyId){
    String[] st = email.split("@");
    return st[0]+ companyId;
  }

  public List<EmployeeDto> getAllEmployeesInCompany(Long companyId) {
    List<Employee> employees = employeeRepo.findByDepartmentCompanyCompanyId(companyId);
    return employees.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public List<EmployeeDto> getAllEmployees() {
    return employeeRepo.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public EmployeeDto getEmployeeById(Long employeeId) {
    Employee employee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> new CustomException("Không tìm thấy nhân viên!", HttpStatus.NOT_FOUND));
    return convertToDto(employee);
  }

  public EmployeeDto updateEmployee(Long employeeId, EmployeeRequest updatedEmployee) {
    Employee existingEmployee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> new CustomException("Không tìm thấy nhân viên!", HttpStatus.NOT_FOUND));

    existingEmployee.setEmployeeName(updatedEmployee.getEmployeeName());
    existingEmployee.setPosition(updatedEmployee.getPosition());
    existingEmployee.setGender(updatedEmployee.getGender());
    existingEmployee.setAddress(updatedEmployee.getAddress());
    existingEmployee.setEmail(updatedEmployee.getEmail());
    existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
    existingEmployee.setDateOfBirth(updatedEmployee.getDateOfBirth());
    existingEmployee.setStatus(updatedEmployee.getStatus());

    return convertToDto(employeeRepo.save(existingEmployee));
  }

  public String updateEmployeeAvatar(Long employeeId, MultipartFile avatarFile) throws IOException {
    Employee employee = employeeRepo.findById(employeeId)
        .orElseThrow(() -> new CustomException("Không tìm thấy nhân viên!", HttpStatus.NOT_FOUND));

    if (avatarFile != null && !avatarFile.isEmpty()) {
      String avatarFileName = awsS3Service.uploadEmployeeAvatar(avatarFile, employeeId);
      employee.setAvatar(avatarFileName);
      employeeRepo.save(employee);
      return awsS3Service.getFileUrl(avatarFileName);
    } else {
      throw new CustomException("Ảnh đại diện không hợp lệ!", HttpStatus.BAD_REQUEST);
    }
  }

  public boolean deleteEmployeeById(Long id) {
    if (employeeRepo.existsById(id)) {
      employeeRepo.deleteById(id);
      return true;
    }
    return false;
  }

  private EmployeeDto convertToDto(Employee employee) {
    EmployeeDto dto = new EmployeeDto();
    dto.setEmployeeId(employee.getEmployeeId());
    dto.setDepartmentId(employee.getDepartment().getDepartmentId());
    dto.setDepartmentName(employee.getDepartment().getDepartmentName());
    dto.setEmployeeCode(employee.getEmployeeCode());
    dto.setEmployeeName(employee.getEmployeeName());
    dto.setPosition(employee.getPosition());
    dto.setGender(employee.getGender());
    dto.setAddress(employee.getAddress());
    dto.setEmail(employee.getEmail());
    dto.setPhoneNumber(employee.getPhoneNumber());
    dto.setDateOfBirth(employee.getDateOfBirth());
    dto.setAvatar(employee.getAvatar());
    dto.setStatus(employee.getStatus());

    if (employee.getAvatar() != null && !employee.getAvatar().isEmpty()) {
      String avatarUrl = awsS3Service.getFileUrl(employee.getAvatar());
      dto.setAvatarUrl(avatarUrl);
    }

    return dto;
  }
}
