package com.scms.scms_be.repository.Purchasing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Purchasing.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    PurchaseOrder findByPoCode(String poCode);

    int countByPoCodeStartingWith(String prefix);

    List<PurchaseOrder> findByCompany_CompanyId(Long companyId);
    List<PurchaseOrder> findBySuplierCompany_CompanyId(Long supplierCompanyId);

    List<PurchaseOrder> findByCompany_CompanyIdAndSuplierCompany_CompanyId(Long companyId, Long supplierCompanyId);
}
