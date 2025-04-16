package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.WarehouseDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.model.request.General.WarehouseRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepo;

    @Autowired
    private CompanyRepository companyRepo;

    public WarehouseDto createWarehouse(Long companyId, WarehouseRequest newWarehouse) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        if (warehouseRepo.existsByWarehouseCode(newWarehouse.getWarehouseCode())) {
            throw new CustomException("Mã kho đã tồn tại!", HttpStatus.BAD_REQUEST);
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setCompany(company);
        warehouse.setWarehouseCode(newWarehouse.getWarehouseCode());
        warehouse.setWarehouseName(newWarehouse.getWarehouseName());
        warehouse.setDescription(newWarehouse.getDescription());
        warehouse.setMaxCapacity(newWarehouse.getMaxCapacity());
        warehouse.setWarehouseType(newWarehouse.getWarehouseType());
        warehouse.setStatus(newWarehouse.getStatus());
        return convertToDto(warehouseRepo.save(warehouse));
    }

    public List<WarehouseDto> getAllWarehousesInCompany(Long companyId) {
        List<Warehouse> warehouses = warehouseRepo.findByCompanyCompanyId(companyId);
        return warehouses.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public WarehouseDto getWarehouseById(Long warehouseId) {
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new CustomException("Kho không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(warehouse);
    }

    public WarehouseDto updateWarehouse(Long warehouseId, WarehouseRequest warehouse) {
        Warehouse existingWarehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new CustomException("Kho không tồn tại!", HttpStatus.NOT_FOUND));

        if (!existingWarehouse.getWarehouseCode().equals(warehouse.getWarehouseCode())
                && warehouseRepo.existsByWarehouseCode(warehouse.getWarehouseCode())) {
            throw new CustomException("Mã kho đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        existingWarehouse.setWarehouseName(warehouse.getWarehouseName());
        existingWarehouse.setDescription(warehouse.getDescription());
        existingWarehouse.setMaxCapacity(warehouse.getMaxCapacity());
        existingWarehouse.setWarehouseType(warehouse.getWarehouseType());
        existingWarehouse.setStatus(warehouse.getStatus());

        return convertToDto(warehouseRepo.save(existingWarehouse));
    }

    public boolean deleteWarehouse(Long warehouseId) {
        Optional<Warehouse> warehouse = warehouseRepo.findById(warehouseId);
        if (warehouse.isPresent()) {
            warehouseRepo.delete(warehouse.get());
            return true;
        }
        return false;
    }

    private WarehouseDto convertToDto(Warehouse warehouse) {
        WarehouseDto dto = new WarehouseDto();
        dto.setWarehouseId(warehouse.getWarehouseId());
        dto.setCompanyId(warehouse.getCompany().getCompanyId());
        dto.setWarehouseCode(warehouse.getWarehouseCode());
        dto.setWarehouseName(warehouse.getWarehouseName());
        dto.setDescription(warehouse.getDescription());
        dto.setMaxCapacity(warehouse.getMaxCapacity());
        dto.setWarehouseType(warehouse.getWarehouseType());
        dto.setStatus(warehouse.getStatus());
        return dto;
    }
}
