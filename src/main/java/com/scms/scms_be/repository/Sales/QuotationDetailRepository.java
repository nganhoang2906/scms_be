package com.scms.scms_be.repository.Sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Sales.QuotationDetail;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetail, Long> {

  List<QuotationDetail> findByQuotation_QuotationId(Long quotationId);

}
