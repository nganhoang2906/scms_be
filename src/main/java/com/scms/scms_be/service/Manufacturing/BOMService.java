package com.scms.scms_be.service.Manufacturing;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.BOMDetailDto;
import com.scms.scms_be.model.dto.Manufacture.BOMDto;
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

    public BOMDto createBOM(BOMRequest request) {
        Item item = itemRepo.findById(request.getItemId())
                .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));
                
        String newBomCode = generateNewBomCode();

        BOM bom = new BOM();
        bom.setItem(item);
        bom.setBomCode(newBomCode);
        bom.setDescription(request.getDescription());
        bom.setStatus("Active");

        BOM savedBOM = bomRepo.save(bom);

        for (BOMRequest.BOMDetailDTO detailDTO : request.getDetails()) {
            if (detailDTO.getItemId().equals(savedBOM.getItem().getItemId())) {
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

        return convertToDto(savedBOM);
    }

    public List<BOMDto> getAllBOMInCom(Long companyId) {
        return bomRepo.findByItem_Company_CompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BOMDto getBOMById(Long bomId) {
        BOM bom = bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("Không tìm thấy BOM!", HttpStatus.NOT_FOUND));
        return convertToDto(bom);
    }

    public BOMDto updateBOM(Long bomId, BOMRequest request) {
        BOM bom = bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("BOM không tồn tại", HttpStatus.NOT_FOUND));

        bom.setDescription(request.getDescription());
        bom.setStatus(request.getStatus());

        BOM updatedBOM = bomRepo.save(bom);
        return convertToDto(updatedBOM);
    }

    public void deleteBOM(Long bomId) {
        if (!bomRepo.existsById(bomId)) {
            throw new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND);
        }
        bomRepo.deleteById(bomId);
    }

    public BOMDto addBomDetail(Long bomId, BOMRequest.BOMDetailDTO detailDTO) {
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
        bomDetailRepo.save(detail);
        BOM newbom = bomRepo.findById(bom.getBomId())
                .orElseThrow(() -> new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(newbom);
    }

    public BOMDto updateBOMDetail(Long bomId, Long bomDetailId, BOMRequest.BOMDetailDTO detailDTO) {
        BOMDetail detail = bomDetailRepo.findById(bomDetailId)
                .orElseThrow(() -> new CustomException("Chi tiết BOM không tồn tại!", HttpStatus.NOT_FOUND));

        BOM bom = bomRepo.findById(bomId)
                .orElseThrow(() -> new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND));

        if (detailDTO.getItemId().equals(bom.getItem().getItemId())) {
            throw new CustomException("Item trong BOMDetail không được trùng với Item của BOM!", HttpStatus.BAD_REQUEST);
        }
        for (BOMDetail existingDetail : bomDetailRepo.findByBom_BomId(bomId)) {
            if (existingDetail.getItem().getItemId().equals(detailDTO.getItemId()) 
                            && !existingDetail.getId().equals(bomDetailId)) {
                throw new CustomException("Item này đã tồn tại trong BOMDetail!", HttpStatus.BAD_REQUEST);
            }
        }
        Item item = itemRepo.findById(detailDTO.getItemId())
                .orElseThrow(() -> new CustomException("Nguyên liệu không tồn tại!", HttpStatus.NOT_FOUND));

        detail.setItem(item);
        detail.setQuantity(detailDTO.getQuantity());
        detail.setNote(detailDTO.getNote());
        bomDetailRepo.save(detail);
        BOM newbom = bomRepo.findById(bom.getBomId())
                .orElseThrow(() -> new CustomException("BOM không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(newbom);
    }

    public void deleteBOMDetail(Long bomDetailId) {
        if (!bomDetailRepo.existsById(bomDetailId)) {
            throw new CustomException("Chi tiết BOM không tồn tại!", HttpStatus.NOT_FOUND);
        }
        bomDetailRepo.deleteById(bomDetailId);
    }

    // ---------------- Helper methods for conversion ----------------

    private String generateNewBomCode() {
        Long count = bomRepo.count();
        return String.format("BOM-%04d", count + 1);
    }

    private BOMDto convertToDto(BOM bom) {
        BOMDto dto = new BOMDto();
        dto.setBomId(bom.getBomId());
        dto.setBomCode(bom.getBomCode());
        dto.setItemId(bom.getItem().getItemId());
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
        dto.setQuantity(detail.getQuantity());
        dto.setNote(detail.getNote());
        return dto;
    }
}
