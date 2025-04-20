package com.scms.scms_be.repository.Sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Sales.SalesOrder;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    SalesOrder findBySoCode(String soCode);

    List<SalesOrder> findByCompany_CompanyId(Long companyId);
    int countBySoCodeStartingWith(String prefix);

    List<SalesOrder> findByPurchaseOrder_PoId(Long poId);
}
