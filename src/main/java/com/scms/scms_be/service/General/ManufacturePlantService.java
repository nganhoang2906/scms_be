package com.scms.scms_be.service.General;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.ManufacturePlant;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ManufacturePlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturePlantService {

    @Autowired
    private ManufacturePlantRepository plantRepo;

    @Autowired
    private CompanyRepository companyRepo;

    // Tạo ManufacturePlant
    public ManufacturePlant createPlant(Long companyId, ManufacturePlant plant) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        if (plantRepo.existsByPlantCode(plant.getPlantCode())) {
            throw new CustomException("Mã nhà máy đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        plant.setCompany(company);
        return plantRepo.save(plant);
    }

    // Lấy tất cả ManufacturePlant của công ty
    public List<ManufacturePlant> getAllPlantsInCompany(Long companyId) {
        return plantRepo.findByCompanyCompanyId(companyId);
    }

    // Lấy ManufacturePlant theo ID
    public ManufacturePlant getPlantById(Long plantId) {
        return plantRepo.findById(plantId)
                .orElseThrow(() -> new CustomException("Nhà máy không tồn tại!", HttpStatus.NOT_FOUND));
    }

    // Cập nhật ManufacturePlant
    public ManufacturePlant updatePlant(Long plantId, ManufacturePlant updatedPlant) {
        ManufacturePlant existingPlant = plantRepo.findById(plantId)
                .orElseThrow(() -> new CustomException("Nhà máy không tồn tại!", HttpStatus.NOT_FOUND));
        
        if(!existingPlant.getPlantCode().equals(updatedPlant.getPlantCode())){
            if (plantRepo.existsByPlantCode(updatedPlant.getPlantCode())) {
                throw new CustomException("Mã nhà máy đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
        }
        existingPlant.setPlantName(updatedPlant.getPlantName());
        existingPlant.setDescription(updatedPlant.getDescription());

        return plantRepo.save(existingPlant);
    }
}
