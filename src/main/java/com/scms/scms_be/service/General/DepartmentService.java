package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.DepartmentDto;
import com.scms.scms_be.model.dto.request.DepartmentRequest;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.DepartmentRepository;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public DepartmentDto createDepartment(DepartmentRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        if (departmentRepository.existsByDepartmentCode(request.getDepartmentCode())) {
            throw new CustomException("Mã phòng ban '" + request.getDepartmentCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        try {
            Department department = new Department();
            department.setCompany(company);
            department.setDepartmentCode(request.getDepartmentCode());
            department.setDepartmentName(request.getDepartmentName());
            
            Department savedDepartment = departmentRepository.save(department);
            return convertToDto(savedDepartment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("Lỗi khi lưu phòng ban!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException("Phòng ban không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(department);
    }

    public List<DepartmentDto> getAllDepartmentInCompany(Long companyId) {
        List<Department> departments = departmentRepository.findByCompanyCompanyId(companyId);
        return departments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public DepartmentDto updateDepartment(Long departmentId, DepartmentRequest updatedDept) {
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException("Phòng ban không tồn tại!", HttpStatus.NOT_FOUND));

        if (!existingDepartment.getDepartmentCode().equals(updatedDept.getDepartmentCode()) &&
                departmentRepository.existsByDepartmentCode(updatedDept.getDepartmentCode())) {
            throw new CustomException("Mã phòng ban '" + updatedDept.getDepartmentCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        existingDepartment.setDepartmentName(updatedDept.getDepartmentName());
        existingDepartment.setDepartmentCode(updatedDept.getDepartmentCode());

        Department savedDepartment = departmentRepository.save(existingDepartment);
        return convertToDto(savedDepartment);
    }

    public boolean deleteDepartmentById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            departmentRepository.delete(department.get());
            return true;
        }
        return false;
    }

    private DepartmentDto convertToDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setCompanyId(department.getCompany().getCompanyId());
        dto.setDepartmentId(department.getDepartmentId());
        dto.setDepartmentCode(department.getDepartmentCode());
        dto.setDepartmentName(department.getDepartmentName());
        return dto;
    }
}