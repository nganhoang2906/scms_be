package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepo;

    @Autowired
    private CompanyRepository companyRepo;

    public Warehouse createWarehouse(Long companyId, Warehouse warehouse) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        if (warehouseRepo.existsByWarehouseCode(warehouse.getWarehouseCode())) {
            throw new CustomException("Mã kho đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        warehouse.setCompany(company);
        return warehouseRepo.save(warehouse);
    }

    public List<Warehouse> getAllWarehousesInCompany(Long companyId) {
        return warehouseRepo.findByCompanyCompanyId(companyId);
    }

    public Warehouse getWarehouseById(Long warehouseId) {
        return warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new CustomException("Kho không tồn tại!", HttpStatus.NOT_FOUND));
    }

    public Warehouse updateWarehouse(Long warehouseId, Warehouse updatedWarehouse) {
        Warehouse existingWarehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new CustomException("Kho không tồn tại!", HttpStatus.NOT_FOUND));

        if (!existingWarehouse.getWarehouseCode().equals(updatedWarehouse.getWarehouseCode())
                && warehouseRepo.existsByWarehouseCode(updatedWarehouse.getWarehouseCode())) {
            throw new CustomException("Mã kho '" + updatedWarehouse.getWarehouseCode() + "' đã tồn tại!", HttpStatus.BAD_REQUEST);
        }
        existingWarehouse.setWarehouseName(updatedWarehouse.getWarehouseName());
        existingWarehouse.setDescription(updatedWarehouse.getDescription());
        existingWarehouse.setMaxCapacity(updatedWarehouse.getMaxCapacity());
        existingWarehouse.setWarehouseType(updatedWarehouse.getWarehouseType());
        existingWarehouse.setStatus(updatedWarehouse.getStatus());

        return warehouseRepo.save(existingWarehouse);
    }
}
