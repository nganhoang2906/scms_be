package com.scms.scms_be.service.Manufacturing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.BOMDetailDto;
import com.scms.scms_be.model.dto.Manufacture.BOMDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.Manufacturing.BOM;
import com.scms.scms_be.model.entity.Manufacturing.BOMDetail;
import com.scms.scms_be.model.request.Manufacturing.BOMDetailRequest;
import com.scms.scms_be.model.request.Manufacturing.BOMRequest;
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

    public BOMDto createBOM(BOMRequest request) {
        Item item = itemRepo.findById(request.getItemId())
                .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));
        Company company = item.getCompany();
        List<BOM> existingBOMs = bomRepo.findByItem_Company_CompanyId(company.getCompanyId());
        for (BOM existingBOM : existingBOMs) {
            if (existingBOM.getItem().getItemId().equals(item.getItemId())) {
                throw new CustomException("Item đã tồn tại trong BOM!", HttpStatus.BAD_REQUEST);
            }
        }

        String newBomCode = generateNewBomCode(item.getItemId());

        BOM bom = new BOM();
        bom.setItem(item);
        bom.setBomCode(newBomCode);
        bom.setDescription(request.getDescription());
        bom.setStatus("Đang hoạt động");

        BOM savedBOM = bomRepo.save(bom);

        for (BOMDetailRequest newdetail : request.getBomDetailList()) {
            if (newdetail.getItemId().equals(savedBOM.getItem().getItemId())) {
                throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
            }

            Item detailItem = itemRepo.findById(newdetail.getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));
            List<BOMDetail> existingDetails = bomDetailRepo.findByBom_BomId(savedBOM.getBomId());
            boolean isDuplicate = existingDetails.stream()
                    .anyMatch(detail -> detail.getItem().getItemId().equals(detailItem.getItemId()));
            if (isDuplicate) {
                throw new CustomException("Item này đã tồn tại trong BOMDetail!", HttpStatus.BAD_REQUEST);
            }


            BOMDetail detail = new BOMDetail();
            detail.setBom(savedBOM);
            detail.setItem(detailItem);
            detail.setQuantity(newdetail.getQuantity());
            detail.setNote(newdetail.getNote());

            bomDetailRepo.save(detail);
        }

        return convertToDto(savedBOM);
    }

    public List<BOMDto> getAllBOMInCom(Long companyId) {
        return bomRepo.findByItem_Company_CompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BOMDto getBOMByItemid(Long itemId) {
        BOM bom = bomRepo.findByItem_ItemId(itemId);
        if (bom == null) {
            throw new CustomException("BOM không tồn tại", HttpStatus.NOT_FOUND);
        }
       return convertToDto(bom);
    }

    public BOMDto updateBOM(Long bomId, BOMRequest request) {
        BOM bom = bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("BOM không tồn tại", HttpStatus.NOT_FOUND));

        bom.setDescription(request.getDescription());
        bom.setStatus(request.getStatus());
        BOM updatedBOM = bomRepo.save(bom);

        List<BOMDetailRequest> detailRequests = request.getBomDetailList();
        List<BOMDetail> existingDetails = bomDetailRepo.findByBom_BomId(bomId);

        // Lấy danh sách itemId từ bomDetailList
        List<Long> detailItemIds = request.getBomDetailList().stream()
                .map(BOMDetailRequest::getItemId)
                .collect(Collectors.toList());

        // Kiểm tra trùng lặp trong danh sách
        Set<Long> uniqueItemIds = new HashSet<>(detailItemIds);
        if (uniqueItemIds.size() < detailItemIds.size()) {
            throw new CustomException("Item trong BOMDetail bị trùng lặp!", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra trùng với itemId của BOM
        if (detailItemIds.contains(request.getItemId())) {
            throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
        }
        
        // Save updated or new details
        for (BOMDetailRequest newDetail : detailRequests) {
            if (newDetail.getItemId().equals(updatedBOM.getItem().getItemId())) {
                throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
            }

            Item item = itemRepo.findById(newDetail.getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));

            BOMDetail matchedDetail = existingDetails.stream()
                    .filter(detail -> detail.getItem().getItemId().equals(newDetail.getItemId()))
                    .findFirst()
                    .orElse(null);

            if (matchedDetail != null) {
                matchedDetail.setQuantity(newDetail.getQuantity());
                matchedDetail.setNote(newDetail.getNote());
                bomDetailRepo.save(matchedDetail);
            } else {
                BOMDetail detail = new BOMDetail();
                detail.setBom(bom);
                detail.setItem(item);
                detail.setQuantity(newDetail.getQuantity());
                detail.setNote(newDetail.getNote());
                bomDetailRepo.save(detail);
            }
        }

        // Delete removed details
        List<Long> newItemIds = detailRequests.stream()
                .map(BOMDetailRequest::getItemId)
                .collect(Collectors.toList());

        for (BOMDetail existingDetail : existingDetails) {
            if (!newItemIds.contains(existingDetail.getItem().getItemId())) {
                bomDetailRepo.delete(existingDetail);
            }
        }

        return convertToDto(updatedBOM);
    }

    public void deleteBOM(Long bomId) {
        if (!bomRepo.existsById(bomId)) {
            throw new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND);
        }
        bomRepo.deleteById(bomId);
    }

    private String generateNewBomCode(Long itemId) {
        String prefix = "BOM" + itemId ;
        int count = bomRepo.countByBomCodeStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }

    private BOMDto convertToDto(BOM bom) {
        BOMDto dto = new BOMDto();
        dto.setBomId(bom.getBomId());
        dto.setBomCode(bom.getBomCode());

        dto.setItemId(bom.getItem().getItemId());
        dto.setItemCode(bom.getItem().getItemCode());
        dto.setItemName(bom.getItem().getItemName());

        dto.setDescription(bom.getDescription());
        dto.setStatus(bom.getStatus());

        List<BOMDetailDto> details = bomDetailRepo.findByBom_BomId(bom.getBomId())
                .stream()
                .map(this::convertToDetailDto)
                .collect(Collectors.toList());

        dto.setBomDetails(details);
        return dto;
    }

    private BOMDetailDto convertToDetailDto(BOMDetail detail) {
        BOMDetailDto dto = new BOMDetailDto();
        dto.setId(detail.getId());
        dto.setBomId(detail.getBom().getBomId());

        dto.setItemId(detail.getItem().getItemId());
        dto.setItemName(detail.getItem().getItemName());
        dto.setItemCode(detail.getItem().getItemCode());
        
        dto.setQuantity(detail.getQuantity());
        dto.setNote(detail.getNote());
        return dto;
    }
}
