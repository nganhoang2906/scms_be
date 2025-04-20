package com.scms.scms_be.repository.Manufacturing;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;

@Repository
public interface ManufactureStageRepository extends JpaRepository<ManufactureStage, Long> {
    ManufactureStage findByItem_ItemId(Long itemId);

    List<ManufactureStage> findByItem_Company_CompanyId(Long companyId); 

}
