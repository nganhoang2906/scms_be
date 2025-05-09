package com.scms.scms_be.repository.General;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.General.ManufacturePlant;

@Repository
public interface ManufacturePlantRepository extends JpaRepository<ManufacturePlant, Long> {
  
  List<ManufacturePlant> findByCompanyCompanyId(Long companyId);

  boolean existsByPlantCode(String plantCode);

  Integer countByPlantCodeStartingWith(String prefix);
  
}
