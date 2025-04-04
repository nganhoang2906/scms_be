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
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ManufacturePlantRepository;

@Service
public class ManufacturePlantService {

    @Autowired
    private ManufacturePlantRepository plantRepo;

    @Autowired
    private CompanyRepository companyRepo;

    // Tạo ManufacturePlant
    public ManufacturePlantDto createPlant(Long companyId, ManufacturePlant plant) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        if (plantRepo.existsByPlantCode(plant.getPlantCode())) {
            throw new CustomException("Mã nhà máy đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        plant.setCompany(company);
        return convertToDto(plantRepo.save(plant));
    }

    // Lấy tất cả ManufacturePlant của công ty
    public List<ManufacturePlantDto> getAllPlantsInCompany(Long companyId) {
        List<ManufacturePlant> plants = plantRepo.findByCompanyCompanyId(companyId);
        return plants.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Lấy ManufacturePlant theo ID
    public ManufacturePlantDto getPlantById(Long plantId) {
        ManufacturePlant plant = plantRepo.findById(plantId)
                .orElseThrow(() -> new CustomException("Nhà máy không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(plant);
    }

    // Cập nhật ManufacturePlant
    public ManufacturePlantDto updatePlant(Long plantId, ManufacturePlant updatedPlant) {
        ManufacturePlant existingPlant = plantRepo.findById(plantId)
                .orElseThrow(() -> new CustomException("Nhà máy không tồn tại!", HttpStatus.NOT_FOUND));

        if (!existingPlant.getPlantCode().equals(updatedPlant.getPlantCode())) {
            if (plantRepo.existsByPlantCode(updatedPlant.getPlantCode())) {
                throw new CustomException("Mã nhà máy đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
        }

        existingPlant.setPlantCode(updatedPlant.getPlantCode());
        existingPlant.setPlantName(updatedPlant.getPlantName());
        existingPlant.setDescription(updatedPlant.getDescription());

        return convertToDto(plantRepo.save(existingPlant));
    }

    // Xóa ManufacturePlant
    public boolean deletePlant(Long plantId) {
        Optional<ManufacturePlant> plant = plantRepo.findById(plantId);
        if (plant.isPresent()) {
            plantRepo.delete(plant.get());
            return true;
        }
        return false;
    }

    // Chuyển entity sang DTO
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
