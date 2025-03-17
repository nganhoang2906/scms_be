package com.scms.scms_be.repository.General;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.General.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByCompanyCompanyId(Long companyId);

    boolean existsByDepartmentCode(String departmentCode);
    int countByCompanyCompanyId(Long companyId);
}
