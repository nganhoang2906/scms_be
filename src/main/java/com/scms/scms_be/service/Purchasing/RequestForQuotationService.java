package com.scms.scms_be.service.Purchasing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Purchasing.RequestForQuotationDto;
import com.scms.scms_be.model.dto.Purchasing.RfqDetailDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.Purchasing.RfqDetail;
import com.scms.scms_be.model.entity.Purchasing.RequestForQuotation;
import com.scms.scms_be.model.request.Purchasing.RequestForQuotationRequest;
import com.scms.scms_be.model.request.Purchasing.RfqDetailRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Purchasing.RfqDetailRepository;
import com.scms.scms_be.repository.Purchasing.RequestForQuotationRepository;

@Service
public class RequestForQuotationService {

    @Autowired
    private RequestForQuotationRepository rfqRepo;

    @Autowired
    private RfqDetailRepository rfqDetailRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private ItemRepository itemRepo;

    public RequestForQuotationDto createRFQ(RequestForQuotationRequest request) {
        Company company = companyRepo.findById(request.getCompanyId())
                .orElseThrow(() -> new CustomException("Company not found", HttpStatus.NOT_FOUND));
        Company requestedCompany = companyRepo.findById(request.getRequestedCompanyId())
                .orElseThrow(() -> new CustomException("Requested company not found", HttpStatus.NOT_FOUND));

        RequestForQuotation rfq = new RequestForQuotation();
        rfq.setCompany(company);
        rfq.setRequestedCompany(requestedCompany);
        rfq.setRfqCode(generateRFQCode(company.getCompanyId(), requestedCompany.getCompanyId()));
        rfq.setNeedByDate(request.getNeedByDate());
        rfq.setCreatedBy(request.getCreatedBy());
        rfq.setCreatedOn(LocalDateTime.now());
        rfq.setLastUpdatedOn(LocalDateTime.now());
        rfq.setStatus(request.getStatus());
   
        RequestForQuotation savedRFQ = rfqRepo.save(rfq);

        for (RfqDetailRequest d : request.getRfqDetails()) {
            Item item = itemRepo.findById(d.getItemId())
                    .orElseThrow(() -> new CustomException("Item not found", HttpStatus.NOT_FOUND));
            RfqDetail detail = new RfqDetail();
            detail.setRfq(savedRFQ);
            detail.setItem(item);
            detail.setQuantity(d.getQuantity());
            detail.setNote(d.getNote());
            rfqDetailRepo.save(detail);
        }

        return convertToDto(savedRFQ);
    }

    public List<RequestForQuotationDto> getAllByCompany(Long companyId) {
        return rfqRepo.findByCompany_CompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RequestForQuotationDto> getAllByRequestCompany( Long requestedCompanyId) {
        return rfqRepo.findByRequestedCompany_CompanyId( requestedCompanyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RequestForQuotationDto getById(Long rfqId) {
        RequestForQuotation rfq = rfqRepo.findById(rfqId)
                .orElseThrow(() -> new CustomException("RFQ not found", HttpStatus.NOT_FOUND));
        return convertToDto(rfq);
    }

    public RequestForQuotationDto updateStatus(Long rfqId, String status) {
        RequestForQuotation rfq = rfqRepo.findById(rfqId)
                .orElseThrow(() -> new CustomException("RFQ not found", HttpStatus.NOT_FOUND));
        rfq.setStatus(status);
        rfq.setLastUpdatedOn(LocalDateTime.now());
        return convertToDto(rfqRepo.save(rfq));
    }

    private String generateRFQCode(Long companyId, Long requestedCompanyId) {
        String prefix = "RFQ" + companyId + requestedCompanyId;
        String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
        int count = rfqRepo.countByRfqCodeStartingWith(prefix);
        return prefix + year + String.format("%04d", count + 1);
    }

    private RequestForQuotationDto convertToDto(RequestForQuotation rfq) {
        RequestForQuotationDto dto = new RequestForQuotationDto();
        dto.setRfqId(rfq.getRfqId());
        dto.setRfqCode(rfq.getRfqCode());
        dto.setCompanyId(rfq.getCompany().getCompanyId());
        dto.setCompanyName(rfq.getCompany().getCompanyName());
        dto.setRequestedCompanyId(rfq.getRequestedCompany().getCompanyId());
        dto.setRequestedCompanyName(rfq.getRequestedCompany().getCompanyName());
        dto.setNeedByDate(rfq.getNeedByDate());
        dto.setCreatedBy(rfq.getCreatedBy());
        dto.setCreatedOn(rfq.getCreatedOn());
        dto.setLastUpdatedOn(rfq.getLastUpdatedOn());
        dto.setStatus(rfq.getStatus());

        List<RfqDetailDto> rfqDetails = rfq.getRfqDetails().stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());
        dto.setRfqDetails(rfqDetails);
        return dto;
    }

    public RfqDetailDto convertToDetailDto(RfqDetail rfqDetail) {
        RfqDetailDto dto = new RfqDetailDto();
        dto.setRfqDetailId(rfqDetail.getRfqDetailId());
        dto.setRfqId(rfqDetail.getRfq().getRfqId());
        dto.setRfqCode(rfqDetail.getRfq().getRfqCode());
        dto.setItemId(rfqDetail.getItem().getItemId());
        dto.setItemName(rfqDetail.getItem().getItemName());
        dto.setItemCode(rfqDetail.getItem().getItemCode());
        dto.setQuantity(rfqDetail.getQuantity());
        dto.setNote(rfqDetail.getNote());
        return dto;
    }

}
