package com.scms.scms_be.repository.Manufacturing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Manufaacturing.BOM;

public interface BOMRepository extends JpaRepository<BOM, Long> {

    boolean existsByBomCode(String bomCode);
    List<BOM> findByItem_Company_CompanyId(Long companyId);

}
