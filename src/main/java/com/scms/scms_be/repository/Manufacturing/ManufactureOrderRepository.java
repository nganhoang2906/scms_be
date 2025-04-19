package com.scms.scms_be.repository.Manufacturing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;

@Repository
public interface ManufactureOrderRepository extends JpaRepository<ManufactureOrder, Long> {


    List<ManufactureOrder> findByItem_ItemId(Long itemId);

    List<ManufactureOrder> findByItem_Company_CompanyId(Long companyId);

    ManufactureOrder findByMoCode(String moCode);

    int countByItemItemIdAndLineLineId(Long itemId, Long lineId);

}
