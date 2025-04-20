package com.scms.scms_be.service.General;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.ItemDto;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.request.General.ItemRequest;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private CompanyRepository companyRepo;

    public ItemDto createItem(Long companyId, ItemRequest newitem) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CustomException("Công ty không tồn tại!", HttpStatus.NOT_FOUND));

        if (itemRepo.existsByItemCode(newitem.getItemCode())) {
            throw new CustomException("Mã mặt hàng đã tồn tại!", HttpStatus.BAD_REQUEST);
        }
        Item item = new Item();
        item.setCompany(company);
        item.setItemCode(generateItemCode(companyId));
        item.setItemName(newitem.getItemName());
        item.setItemType(newitem.getItemType());
        item.setUom(newitem.getUom());
        item.setTechnicalSpecifications(newitem.getTechnicalSpecifications());
        item.setImportPrice(newitem.getImportPrice());
        item.setExportPrice(newitem.getExportPrice());
        item.setDescription(newitem.getDescription());
        return convertToDto(itemRepo.save(item));
    }
    public String generateItemCode(Long companyId) {
        String prefix = "T"+String.format("%04d", companyId);
        int count = itemRepo.countByItemCodeStartingWith(prefix);
        return prefix + String.format("%05d", count + 1);
    }

    public List<ItemDto> getAllItemsInCompany(Long companyId) {
        List<Item> items = itemRepo.findByCompanyCompanyId(companyId);
        return items.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ItemDto getItemById(Long itemId) {
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Không tìm thấy mặt hàng!", HttpStatus.NOT_FOUND));
        return convertToDto(item);
    }

    public ItemDto updateItem(Long itemId, ItemRequest updatedItem) {
        Item existingItem = itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Item không tồn tại!", HttpStatus.NOT_FOUND));

        if (!existingItem.getItemCode().equals(updatedItem.getItemCode())) {
            if (itemRepo.existsByItemCode(updatedItem.getItemCode())) {
                throw new CustomException("Mã mặt hàng đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
        }
        existingItem.setItemName(updatedItem.getItemName());
        existingItem.setItemType(updatedItem.getItemType());
        existingItem.setUom(updatedItem.getUom());
        existingItem.setTechnicalSpecifications(updatedItem.getTechnicalSpecifications());
        existingItem.setImportPrice(updatedItem.getImportPrice());
        existingItem.setExportPrice(updatedItem.getExportPrice());
        existingItem.setDescription(updatedItem.getDescription());
        return convertToDto(itemRepo.save(existingItem));
    }

    public boolean deleteItem(Long itemId) {
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isPresent()) {
            itemRepo.delete(item.get());
            return true;
        }
        return false;
    }

    private ItemDto convertToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setCompanyId(item.getCompany().getCompanyId());
        dto.setItemId(item.getItemId());
        dto.setItemCode(item.getItemCode());
        dto.setItemName(item.getItemName());
        dto.setItemType(item.getItemType());
        dto.setUom(item.getUom());
        dto.setTechnicalSpecifications(item.getTechnicalSpecifications());
        dto.setImportPrice(item.getImportPrice());
        dto.setExportPrice(item.getExportPrice());
        dto.setDescription(item.getDescription());

        return dto;
    }
}
