package com.scms.scms_be.service.General;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepo;

    @Autowired
    private CompanyRepository companyRepo;

    // Tạo Warehouse
    public Warehouse createWarehouse(Long companyId, Warehouse warehouse) {
        // Kiểm tra công ty có tồn tại không
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        // Kiểm tra mã warehouseCode có bị trùng không
        if (warehouseRepo.existsByWarehouseCode(warehouse.getWarehouseCode())) {
            throw new CustomException("Mã kho đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        warehouse.setCompany(company);
        return warehouseRepo.save(warehouse);
    }

    // Lấy tất cả Warehouse của công ty
    public List<Warehouse> getAllWarehousesInCompany(Long companyId) {
        return warehouseRepo.findByCompanyCompanyId(companyId);
    }

    // Lấy Warehouse theo ID
    public Warehouse getWarehouseById(Long warehouseId) {
        return warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new CustomException("Kho không tồn tại!", HttpStatus.NOT_FOUND));
    }

    // Cập nhật Warehouse
    public Warehouse updateWarehouse(Long warehouseId, Warehouse updatedWarehouse) {
        Warehouse existingWarehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new CustomException("Kho không tồn tại!", HttpStatus.NOT_FOUND));

        // Cập nhật thông tin kho
        if (!existingWarehouse.getWarehouseCode().equals(updatedWarehouse.getWarehouseCode()) &&
                warehouseRepo.existsByWarehouseCode(updatedWarehouse.getWarehouseCode())) {
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
