package com.scms.scms_be.service.General;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.DepartmentDto;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.repository.General.DepartmentRepository;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;


    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException("Bộ phận không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(department);
    }

    public List<DepartmentDto> getAllDepartmentInCompany(Long companyId) {
        List<Department> departments = departmentRepository.findByCompanyCompanyId(companyId);
        return departments.stream().map(this::convertToDto).collect(Collectors.toList());
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