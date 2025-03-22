package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
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


    public Department createDepartment(DepartmentRequest request) {
        //   Kiểm tra công ty có tồn tại không
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        //   Kiểm tra xem `departmentCode` đã tồn tại chưa
        if (departmentRepository.existsByDepartmentCode(request.getDepartmentCode())) {
            throw new CustomException("Mã phòng ban '" + request.getDepartmentCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        try {
            //   Tạo `Department` với `Company`
            Department department = new Department();
            department.setCompany(company);
            department.setDepartmentCode(request.getDepartmentCode());
            department.setDepartmentName(request.getDepartmentName());

            return departmentRepository.save(department);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException("Lỗi khi lưu phòng ban! Mã phòng ban đã tồn tại.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException("Phòng ban không tồn tại!", HttpStatus.NOT_FOUND));
    }

    public List<Department> getAllDepartmentInCompany(Long companyId){
        return departmentRepository.findByCompanyCompanyId(companyId);
    }

    public Department updateDepartment(Long departmentId, Department updatedDept){

        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException("Phòng ban không tồn tại!", HttpStatus.NOT_FOUND));

        //  Kiểm tra xem `department_code` đã tồn tại chưa (trừ chính nó)
        if (!existingDepartment.getDepartmentCode().equals(updatedDept.getDepartmentCode()) &&
                departmentRepository.existsByDepartmentCode(updatedDept.getDepartmentCode())) {
            throw new CustomException("Mã phòng ban '" + updatedDept.getDepartmentCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        existingDepartment.setDepartmentName(updatedDept.getDepartmentName());
        existingDepartment.setDepartmentCode(updatedDept.getDepartmentCode());

        return departmentRepository.save(existingDepartment);
    }

    
}
