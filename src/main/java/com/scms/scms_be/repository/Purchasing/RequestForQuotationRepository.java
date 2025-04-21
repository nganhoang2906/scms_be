package com.scms.scms_be.repository.Purchasing;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.scms.scms_be.model.entity.Purchasing.RequestForQuotation;

public interface RequestForQuotationRepository extends JpaRepository<RequestForQuotation, Long> {

    List<RequestForQuotation> findByCompany_CompanyId(Long companyId);

    int countByRfqCodeStartingWith(String prefix);

    List<RequestForQuotation> findByRequestedCompany_CompanyId(Long requestedCompanyId);

}
