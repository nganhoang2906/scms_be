package com.scms.scms_be.controller.General;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.CompanyDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.service.General.CompanyService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/auth/get-company/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        CompanyDto company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/auth/get-all-companies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/comsys/update-company/{companyId}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long companyId, 
                         @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompany(companyId, companyDto);
        return ResponseEntity.ok(updatedCompany);
        }

}