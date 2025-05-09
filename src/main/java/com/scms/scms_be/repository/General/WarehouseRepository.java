package com.scms.scms_be.repository.General;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scms.scms_be.model.entity.General.Warehouse;
import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
  
  List<Warehouse> findByCompanyCompanyId(Long companyId);

  boolean existsByWarehouseCode(String warehouseCode);
  
}
