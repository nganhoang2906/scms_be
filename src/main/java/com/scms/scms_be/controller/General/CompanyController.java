package com.scms.scms_be.controller.General;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.CompanyDto;
import com.scms.scms_be.model.request.General.CompanyRequest;
import com.scms.scms_be.service.General.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CompanyController {

  @Autowired
  private CompanyService companyService;

  @GetMapping("/user/get-company/{id}")
  public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
    CompanyDto company = companyService.getCompanyById(id);
    return ResponseEntity.ok(company);
  }

  @GetMapping("/user/get-all-companies")
  public ResponseEntity<List<CompanyDto>> getAllCompanies() {
    List<CompanyDto> companies = companyService.getAllCompanies();
    return ResponseEntity.ok(companies);
  }

  @PutMapping("/comsys/update-company/{companyId}")
  public ResponseEntity<CompanyDto> updateCompany(
      @PathVariable Long companyId,
      @RequestBody CompanyRequest companyDto) {
    CompanyDto updatedCompany = companyService.updateCompany(companyId, companyDto);
    return ResponseEntity.ok(updatedCompany);
  }

  @PutMapping("/comsys/update-company-logo/{id}")
  public ResponseEntity<String> updateCompanyLogo(
      @PathVariable Long id,
      @RequestParam("logo") MultipartFile logoFile) {

    try {
      String logoUrl = companyService.updateCompanyLogo(id, logoFile);
      return ResponseEntity.ok(logoUrl);
    } catch (IOException e) {
      throw new CustomException("Cập nhật logo thất bại!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
}