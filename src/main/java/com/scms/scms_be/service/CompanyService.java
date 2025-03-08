package com.scms.scms_be.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scms.scms_be.model.entity.Company;
import com.scms.scms_be.repository.CompanyRepository;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompanyCode(companyDetails.getCompanyCode());
        company.setTaxCode(companyDetails.getTaxCode());
        company.setCompanyName(companyDetails.getCompanyName());
        company.setHeadOfficeAddress(companyDetails.getHeadOfficeAddress());
        company.setMainIndustry(companyDetails.getMainIndustry());
        company.setRepresentativeName(companyDetails.getRepresentativeName());
        company.setStartDate(companyDetails.getStartDate());
        company.setJoinDate(companyDetails.getJoinDate());
        company.setCompanyType(companyDetails.getCompanyType());
        company.setPhoneNumber(companyDetails.getPhoneNumber());
        company.setEmail(companyDetails.getEmail());
        company.setWebsite(companyDetails.getWebsite());
        company.setLogo(companyDetails.getLogo());
        company.setStatus(companyDetails.getStatus());
        company.setCountry(companyDetails.getCountry());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}