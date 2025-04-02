package com.scms.scms_be.service.Manufacturing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.request.BOMRequest;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.Manufacturing.BOM;
import com.scms.scms_be.model.entity.Manufacturing.BOMDetail;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Manufacturing.BOMDetailRepository;
import com.scms.scms_be.repository.Manufacturing.BOMRepository;

@Service
public class BOMService {

    @Autowired
    private BOMRepository bomRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private BOMDetailRepository bomDetailRepo;

    public BOM createBOM(BOMRequest request) {
        Item item = itemRepo.findById(request.getItemId())
                .orElseThrow(() -> new CustomException("Mặt hàng không tồn tại", HttpStatus.NOT_FOUND));

        String newBomCode = generateNewBomCode();

        BOM bom = new BOM();
        bom.setItem(item);
        bom.setBomCode(newBomCode);
        bom.setDescription(request.getDescription());
        bom.setStatus(request.getStatus());

        BOM savedBOM = bomRepo.save(bom);

        // Tạo BOMDetails
        for (BOMRequest.BOMDetailDTO detailDTO : request.getDetails()) {
            if (detailDTO.getItemId().equals(bom.getItem().getItemId())) {
                throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
            }
            
            Item detailItem = itemRepo.findById(detailDTO.getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));

            BOMDetail detail = new BOMDetail();
            detail.setBom(savedBOM);
            detail.setItem(detailItem);
            detail.setQuantity(detailDTO.getQuantity());
            detail.setNote(detailDTO.getNote());

            bomDetailRepo.save(detail);
        }
        return savedBOM;
    }

    public List<BOM> getAllBOMInCom( Long companyId) {
        return bomRepo.findByItem_Company_CompanyId(companyId);
    }

    public BOM getBOMById(Long bomId) {
        return bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("Không tìm thấy BOM!", HttpStatus.NOT_FOUND));
    }

    public BOM updateBOM(Long bomId, BOMRequest request) {
        BOM bom = bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("BOM không tồn tại", HttpStatus.NOT_FOUND));
        
        // Cập nhật BOM
        bom.setDescription(request.getDescription());
        bom.setStatus(request.getStatus());
        BOM updatedBOM = bomRepo.save(bom);
    
        return updatedBOM;
    }
    
    public void deleteBOM(Long bomId) {
        if (!bomRepo.existsById(bomId)) {
            throw new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND);
        }
        bomRepo.deleteById(bomId);
    }

    private String generateNewBomCode() {
        Long count = bomRepo.count();
        return String.format("BOM-%04d", count + 1); // BOM-0001, BOM-0002,...
    }

    public BOMDetail addBomDetail(Long bomId, BOMRequest.BOMDetailDTO detailDTO) {
        BOM bom = bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND));
        
        if (detailDTO.getItemId().equals(bom.getItem().getItemId())) {
            throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
        }
        List<BOMDetail> existingDetails = bomDetailRepo.findByBom_BomId(bomId);
        boolean isDuplicate = existingDetails.stream()
                .anyMatch(detail -> detail.getItem().getItemId().equals(detailDTO.getItemId()));
        if (isDuplicate) {
            throw new CustomException("Item này đã tồn tại trong BOMDetail!", HttpStatus.BAD_REQUEST);
        }
                
        Item item = itemRepo.findById(detailDTO.getItemId())
                .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));

        BOMDetail detail = new BOMDetail();
        detail.setBom(bom);
        detail.setItem(item);
        detail.setQuantity(detailDTO.getQuantity());
        detail.setNote(detailDTO.getNote());

        return bomDetailRepo.save(detail);
    }

    public BOMDetail updateBOMDetail(Long bomId , Long bomDetailId, BOMRequest.BOMDetailDTO detailDTO) {
        BOMDetail detail = bomDetailRepo.findById(bomDetailId)
                .orElseThrow(() -> new CustomException("Chi tiết BOM không tồn tại!", HttpStatus.NOT_FOUND));
        
                BOM bom = bomRepo.findById(bomDetailId)
                .orElseThrow(() -> new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND));
        
        if (detailDTO.getItemId().equals(bom.getItem().getItemId())) {
            throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
        }
                
        Item item = itemRepo.findById(detailDTO.getItemId())
                .orElseThrow(() -> new CustomException("Nguyên liệu không tồn tại!", HttpStatus.NOT_FOUND));

        detail.setItem(item);
        detail.setQuantity(detailDTO.getQuantity());
        detail.setNote(detailDTO.getNote());

        return bomDetailRepo.save(detail);
    }

    public void deleteBOMDetail(Long bomDetailId) {
        if (!bomDetailRepo.existsById(bomDetailId)) {
            throw new CustomException("Chi tiết BOM không tồn tại!", HttpStatus.NOT_FOUND);
        }
        bomDetailRepo.deleteById(bomDetailId);
    }

}
