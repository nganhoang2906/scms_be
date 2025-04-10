package com.scms.scms_be.service.General;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.CompanyDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.repository.General.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AwsS3Service awsS3Service;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date joinDate = sdf.parse(sdf.format(new Date())); 
            company.setJoinDate(joinDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Cập nhật thông tin công ty
        company.setCompanyCode(companyDetails.getCompanyCode());
        company.setAddress(companyDetails.getAddress());
        company.setTaxCode(companyDetails.getTaxCode());
        company.setCompanyName(companyDetails.getCompanyName());
        company.setMainIndustry(companyDetails.getMainIndustry());
        company.setRepresentativeName(companyDetails.getRepresentativeName());
        company.setStartDate(companyDetails.getStartDate());
        company.setCompanyType(companyDetails.getCompanyType());
        company.setPhoneNumber(companyDetails.getPhoneNumber());
        company.setEmail(companyDetails.getEmail());
        company.setWebsiteAddress(companyDetails.getWebsiteAddress());
        company.setStatus(companyDetails.getStatus());
        company.setCountry(companyDetails.getCountry());

        companyRepository.save(company);
        return convertToDto(company);
    }

    public String updateCompanyLogo(Long companyId, MultipartFile logoFile) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));
    
        if (logoFile != null && !logoFile.isEmpty()) {
            String logoFileName = awsS3Service.uploadCompanyLogo(logoFile, companyId);
            company.setLogo(logoFileName);
            companyRepository.save(company);
            return awsS3Service.getFileUrl(logoFileName);
        } else {
            throw new CustomException("Logo không hợp lệ", HttpStatus.BAD_REQUEST);
        }
    }
    

    private CompanyDto getCompanyDtoById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));
        
                CompanyDto companyDto = convertToDto(company);

        if (company.getLogo() != null && !company.getLogo().isEmpty()) {
            String logoUrl = awsS3Service.getFileUrl(company.getLogo()); // Lấy URL của logo từ S3
            companyDto.setLogoUrl(logoUrl); // Set URL vào DTO
        }
    
        return companyDto;
    }

    private CompanyDto convertToDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setCompanyId(company.getCompanyId());
        dto.setCompanyCode(company.getCompanyCode());
        dto.setCompanyName(company.getCompanyName());
        dto.setAddress(company.getAddress());
        dto.setTaxCode(company.getTaxCode());
        dto.setMainIndustry(company.getMainIndustry());
        dto.setRepresentativeName(company.getRepresentativeName());
        dto.setStartDate(company.getStartDate());
        dto.setJoinDate(company.getJoinDate());
        dto.setCompanyType(company.getCompanyType());
        dto.setPhoneNumber(company.getPhoneNumber());
        dto.setEmail(company.getEmail());
        dto.setWebsiteAddress(company.getWebsiteAddress());
        dto.setStatus(company.getStatus());
        dto.setCountry(company.getCountry());
        dto.setLogo(company.getLogo());
        
        if (company.getLogo() != null && !company.getLogo().isEmpty()) {
            String logoUrl = awsS3Service.getFileUrl(company.getLogo());
            dto.setLogoUrl(logoUrl);
        }
    
        return dto;
    }
}
