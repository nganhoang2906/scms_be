package com.scms.scms_be.service.Sales;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Sales.QuotationDetailDto;
import com.scms.scms_be.model.dto.Sales.QuotationDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.Purchasing.RequestForQuotation;
import com.scms.scms_be.model.entity.Sales.Quotation;
import com.scms.scms_be.model.entity.Sales.QuotationDetail;
import com.scms.scms_be.model.request.Sales.QuotationDetailRequest;
import com.scms.scms_be.model.request.Sales.QuotationRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Purchasing.RequestForQuotationRepository;
import com.scms.scms_be.repository.Sales.QuotationDetailRepository;
import com.scms.scms_be.repository.Sales.QuotationRepository;

@Service
public class QuotationService {

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private QuotationDetailRepository quotationDetailRepository;

  @Autowired
  private RequestForQuotationRepository rfqRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private ItemRepository itemRepository;

  public QuotationDto createQuotation(QuotationRequest request) {
    Company company = companyRepository.findById(request.getCompanyId())
        .orElseThrow(() -> new CustomException("Không tìm thấy công ty", HttpStatus.NOT_FOUND));

    RequestForQuotation rfq = rfqRepository.findById(request.getRfqId())
        .orElseThrow(() -> new CustomException("Không tìm thấy yêu cầu báo giá!", HttpStatus.NOT_FOUND));

    Quotation quotation = new Quotation();
    quotation.setCompany(company);
    quotation.setRfq(rfq);
    quotation.setQuotationCode(generateQuotationCode(company.getCompanyId(), rfq.getRfqId()));
    quotation.setAvailableByDate(request.getAvailableByDate());
    quotation.setCreatedBy(request.getCreatedBy());
    quotation.setCreatedOn(LocalDateTime.now());
    quotation.setLastUpdatedOn(LocalDateTime.now());
    quotation.setStatus(request.getStatus());

    Quotation savedQuotation = quotationRepository.save(quotation);

    if (request.getQuotationDetails() == null || request.getQuotationDetails().isEmpty()) {
      throw new CustomException("Danh sách hàng hóa không được để trống!", HttpStatus.BAD_REQUEST);
    }
    Double totalPrice = 0.0;
    for (QuotationDetailRequest rfqDetail : request.getQuotationDetails()) {
      QuotationDetail detail = new QuotationDetail();
      Item item = itemRepository.findById(rfqDetail.getItemId())
          .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND));

      detail.setQuotation(savedQuotation);
      detail.setItem(item);
      detail.setQuantity(rfqDetail.getQuantity());
      detail.setItemPrice(item.getExportPrice());
      detail.setNote(rfqDetail.getNote());
      quotationDetailRepository.save(detail);
      totalPrice += item.getExportPrice() * rfqDetail.getQuantity();
    }
    savedQuotation.setTotalPrice(totalPrice);
    return convertToDto(savedQuotation);
  }

  public QuotationDto getQuotationById(Long quotationId) {
    Quotation quotation = quotationRepository.findById(quotationId)
        .orElseThrow(() -> new CustomException("Không tìm thấy báo giá!", HttpStatus.NOT_FOUND));
    return convertToDto(quotation);
  }

  public List<QuotationDto> getAllQuotationsByCompany(Long companyId) {
    List<Quotation> quotations = quotationRepository.findByCompany_CompanyId(companyId);
    return quotations.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public List<QuotationDto> getAllQuotationsByRfq(Long rfqId) {
    List<Quotation> quotations = quotationRepository.findByRfq_RfqId(rfqId);
    return quotations.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public QuotationDto convertToDto(Quotation quotation) {
    QuotationDto dto = new QuotationDto();
    dto.setQuotationId(quotation.getQuotationId());
    dto.setQuotationCode(quotation.getQuotationCode());
    dto.setCompanyId(quotation.getCompany().getCompanyId());
    dto.setCompanyName(quotation.getCompany().getCompanyName());
    dto.setRfqId(quotation.getRfq().getRfqId());
    dto.setRfqCode(quotation.getRfq().getRfqCode());
    dto.setTotalPrice(quotation.getTotalPrice());
    dto.setAvailableByDate(quotation.getAvailableByDate());
    dto.setCreatedBy(quotation.getCreatedBy());
    dto.setCreatedOn(quotation.getCreatedOn());
    dto.setLastUpdatedOn(quotation.getLastUpdatedOn());
    dto.setStatus(quotation.getStatus());

    List<QuotationDetailDto> detailDtos = quotationDetailRepository
        .findByQuotation_QuotationId(quotation.getQuotationId())
        .stream()
        .map(this::convertToDetailDto)
        .collect(Collectors.toList());
    dto.setQuotationDetails(detailDtos);

    return dto;
  }

  public QuotationDetailDto convertToDetailDto(QuotationDetail detail) {
    QuotationDetailDto dto = new QuotationDetailDto();
    dto.setQuotaionDetailId(detail.getQuotaionDetailId());
    dto.setQuotationId(detail.getQuotation().getQuotationId());
    dto.setItemId(detail.getItem().getItemId());
    dto.setItemName(detail.getItem().getItemName());
    dto.setItemCode(detail.getItem().getItemCode());
    dto.setItemPrice(detail.getItemPrice());
    dto.setQuantity(detail.getQuantity());
    dto.setNote(detail.getNote());
    return dto;
  }

  private String generateQuotationCode(Long companyId, Long rfqId) {
    String prefix = "QTT" + companyId + rfqId;
    String year = String.valueOf(LocalDateTime.now().getYear()).substring(2);
    int count = quotationRepository.countByQuotationCodeStartingWith(prefix);
    return prefix + year + String.format("%04d", count + 1);
  }

}
