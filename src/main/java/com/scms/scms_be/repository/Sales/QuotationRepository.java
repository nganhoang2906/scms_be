package com.scms.scms_be.repository.Sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Sales.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

  int countByQuotationCodeStartingWith(String prefix);

  List<Quotation> findByRfq_RfqId(Long rfqId);

  List<Quotation> findByCompany_CompanyId(Long companyId);

}
