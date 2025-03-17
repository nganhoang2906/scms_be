package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.UserRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company updateCompany(Long companyId,  Company companyDetails) {

        // Kiểm tra công ty có tồn tại không
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        // Kiểm tra `companyCode` có bị trùng không
        if (!company.getCompanyCode().equals(companyDetails.getCompanyCode()) &&
                companyRepository.existsByCompanyCode(companyDetails.getCompanyCode())) {
            throw new CustomException("Mã công ty '" + companyDetails.getCompanyCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra `taxCode` có bị trùng không
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

        return companyRepository.save(company);
    }

    public void deleteCompany(Long companyId) {
        // Kiểm tra công ty có tồn tại không
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        //   Xóa công ty
        companyRepository.delete(company);
        }
}