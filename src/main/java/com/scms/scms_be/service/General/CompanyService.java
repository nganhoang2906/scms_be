package com.scms.scms_be.service.General;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.CompanyDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.repository.General.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyDto getCompanyById(Long id) {
        return getCompanyDtoById(id);
    }

    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CompanyDto updateCompany(Long companyId, CompanyDto companyDetails) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        // Kiểm tra 'companyCode' có bị trùng không
        if (!company.getCompanyCode().equals(companyDetails.getCompanyCode()) &&
                companyRepository.existsByCompanyCode(companyDetails.getCompanyCode())) {
            throw new CustomException("Mã công ty '" + companyDetails.getCompanyCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra 'taxCode' có bị trùng không
        if (!company.getTaxCode().equals(companyDetails.getTaxCode()) &&
                companyRepository.existsByTaxCode(companyDetails.getTaxCode())) {
            throw new CustomException("Mã số thuế '" + companyDetails.getTaxCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        // Cập nhật thông tin công ty
        company.setCompanyCode(companyDetails.getCompanyCode());
        company.setTaxCode(companyDetails.getTaxCode());
        company.setCompanyName(companyDetails.getCompanyName());
        company.setMainIndustry(companyDetails.getMainIndustry());
        company.setRepresentativeName(companyDetails.getRepresentativeName());
        company.setStartDate(companyDetails.getStartDate());
        company.setJoinDate(companyDetails.getJoinDate());
        company.setCompanyType(companyDetails.getCompanyType());
        company.setPhoneNumber(companyDetails.getPhoneNumber());
        company.setEmail(companyDetails.getEmail());
        company.setWebsiteAddress(companyDetails.getWebsiteAddress());
        company.setLogo(companyDetails.getLogo());
        company.setStatus(companyDetails.getStatus());
        company.setCountry(companyDetails.getCountry());

        companyRepository.save(company);
        return convertToDto(company);
    }

    private CompanyDto getCompanyDtoById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(company);
    }

    private CompanyDto convertToDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setCompanyId(company.getCompanyId());
        dto.setCompanyCode(company.getCompanyCode());
        dto.setCompanyName(company.getCompanyName());
        dto.setTaxCode(company.getTaxCode());
        dto.setMainIndustry(company.getMainIndustry());
        dto.setRepresentativeName(company.getRepresentativeName());
        dto.setStartDate(company.getStartDate());
        dto.setJoinDate(company.getJoinDate());
        dto.setCompanyType(company.getCompanyType());
        dto.setPhoneNumber(company.getPhoneNumber());
        dto.setEmail(company.getEmail());
        dto.setWebsiteAddress(company.getWebsiteAddress());
        dto.setLogo(company.getLogo());
        dto.setStatus(company.getStatus());
        dto.setCountry(company.getCountry());
        return dto;
    }
}
