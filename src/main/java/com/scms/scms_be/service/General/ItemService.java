package com.scms.scms_be.service.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private CompanyRepository companyRepo;

    public Item createItem(Long companyId, Item item) {
        if (itemRepo.existsByItemCode(item.getItemCode())) {
            throw new CustomException("Mã mặt hàng đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        item.setCompany(company);
        return itemRepo.save(item);
    }

    public List<Item> getAllItemsByCompany(Long companyId) {
        return itemRepo.findByCompanyCompanyId(companyId);
    }

    public Item getItemById(Long itemId) {
        return itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Không tìm thấy mặt hàng!", HttpStatus.NOT_FOUND));
    }

    public Item updateItem(Long itemId, Item updated) {
        Item existing = getItemById(itemId);
        if (!existing.getItemCode().equals(updated.getItemCode())) {
            if (itemRepo.existsByItemCode(updated.getItemCode())) {
                throw new CustomException("Mã mặt hàng đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
        }
        existing.setItemName(updated.getItemName());
        existing.setItemType(updated.getItemType());
        existing.setUom(updated.getUom());
        existing.setTechnicalSpecifications(updated.getTechnicalSpecifications());
        existing.setUnitPrice(updated.getUnitPrice());
        existing.setDescription(updated.getDescription());
        return itemRepo.save(existing);
    }

    public void deleteItem(Long itemId) {
        Item existing = getItemById(itemId);
        itemRepo.delete(existing);
    
    }
}
