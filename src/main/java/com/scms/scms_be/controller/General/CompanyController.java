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

import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.service.General.CompanyService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/get-company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        Optional<Company> company = companyService.getCompanyById(companyId);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all-companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @PutMapping("/sysad/update-company/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long companyId , @RequestBody Company companyDetails) {
        Company updatedCompany = companyService.updateCompany(companyId,companyDetails);
        return ResponseEntity.ok(updatedCompany);
    }

}