package com.scms.scms_be.service.Manufacturing;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.Manufaacturing.BOM;
import com.scms.scms_be.repository.Manufacturing.BOMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BOMService {

    @Autowired
    private BOMRepository bomRepo;

    public BOM createBOM(BOM bom) {
        if (bomRepo.existsByBomCode(bom.getBomCode())) {
            throw new CustomException("Mã BOM đã tồn tại!", HttpStatus.BAD_REQUEST);
        }
        return bomRepo.save(bom);
    }

    public List<BOM> getAllBOMInCom( Long companyId) {
        return bomRepo.findByItem_Company_CompanyId(companyId);
    }

    public BOM getBOMById(Long bomId) {
        return bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("Không tìm thấy BOM!", HttpStatus.NOT_FOUND));
    }

    // public BOM updateBOM(Long bomId, BOM updated) {
    //     BOM bom = getBOMById(bomId);

    //     if (!bom.getBomCode().equals(updated.getBomCode()) &&
    //         bomRepo.existsByBomCode(updated.getBomCode())) {
    //         throw new CustomException("Mã BOM mới đã tồn tại!", HttpStatus.BAD_REQUEST);
    //     }

    //     bom.setBomCode(updated.getBomCode());
    //     bom.setDescription(updated.getDescription());
    //     bom.setStatus(updated.getStatus());
    //     bom.setItem(updated.getItemId());

    //     return bomRepo.save(bom);
    // }
}
