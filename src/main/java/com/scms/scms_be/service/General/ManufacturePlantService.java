package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.ManufacturePlantDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.ManufacturePlant;
import com.scms.scms_be.model.request.General.ManuPlantRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ManufacturePlantRepository;

@Service
public class ManufacturePlantService {

  @Autowired
  private ManufacturePlantRepository plantRepo;

  @Autowired
  private CompanyRepository companyRepo;

  public ManufacturePlantDto createPlant(Long companyId, ManuPlantRequest newPlant) {
    Company company = companyRepo.findById(companyId)
        .orElseThrow(() -> new CustomException("Không tìm thấy công ty!", HttpStatus.NOT_FOUND));

    if (plantRepo.existsByPlantCode(newPlant.getPlantCode())) {
      throw new CustomException("Mã xưởng đã được sử dụng!", HttpStatus.BAD_REQUEST);
    }
    ManufacturePlant plant = new ManufacturePlant();
    plant.setCompany(company);
    plant.setPlantCode(generatePlantCode(companyId));
    plant.setPlantName(newPlant.getPlantName());
    plant.setDescription(newPlant.getDescription());
    return convertToDto(plantRepo.save(plant));
  }

  public String generatePlantCode(Long companyId) {
    String prefix = "MP" + String.format("%04d", companyId);
    int count = plantRepo.countByPlantCodeStartingWith(prefix);
    return prefix + String.format("%03d", count + 1);
  }

  public List<ManufacturePlantDto> getAllPlantsInCompany(Long companyId) {
    List<ManufacturePlant> plants = plantRepo.findByCompanyCompanyId(companyId);
    return plants.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public ManufacturePlantDto getPlantById(Long plantId) {
    ManufacturePlant plant = plantRepo.findById(plantId)
        .orElseThrow(() -> new CustomException("Không tìm thấy xưởng sản xuất!", HttpStatus.NOT_FOUND));
    return convertToDto(plant);
  }

  public ManufacturePlantDto updatePlant(Long plantId, ManuPlantRequest updatedPlant) {
    ManufacturePlant existingPlant = plantRepo.findById(plantId)
        .orElseThrow(() -> new CustomException("Không tìm thấy xưởng sản xuất!", HttpStatus.NOT_FOUND));

    if (!existingPlant.getPlantCode().equals(updatedPlant.getPlantCode())) {
      if (plantRepo.existsByPlantCode(updatedPlant.getPlantCode())) {
        throw new CustomException("Mã xưởng đã được sử dụng!", HttpStatus.BAD_REQUEST);
      }
    }

    existingPlant.setPlantCode(updatedPlant.getPlantCode());
    existingPlant.setPlantName(updatedPlant.getPlantName());
    existingPlant.setDescription(updatedPlant.getDescription());

    return convertToDto(plantRepo.save(existingPlant));
  }

  public boolean deletePlant(Long plantId) {
    Optional<ManufacturePlant> plant = plantRepo.findById(plantId);
    if (plant.isPresent()) {
      plantRepo.delete(plant.get());
      return true;
    }
    return false;
  }

  private ManufacturePlantDto convertToDto(ManufacturePlant plant) {
    ManufacturePlantDto dto = new ManufacturePlantDto();
    dto.setPlantId(plant.getPlantId());
    dto.setPlantCode(plant.getPlantCode());
    dto.setPlantName(plant.getPlantName());
    dto.setDescription(plant.getDescription());
    dto.setCompanyId(plant.getCompany().getCompanyId());
    return dto;
  }
  
}
