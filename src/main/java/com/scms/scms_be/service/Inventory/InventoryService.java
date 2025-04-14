package com.scms.scms_be.service.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Inventory.InventoryDto;
import com.scms.scms_be.model.dto.request.InventoryRequest;
import com.scms.scms_be.model.entity.General.Item;
import com.scms.scms_be.model.entity.General.Warehouse;
import com.scms.scms_be.model.entity.Inventory.Inventory;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.WarehouseRepository;
import com.scms.scms_be.repository.Inventory.InventoryRepository;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    public List<InventoryDto> getAllInventoryByItem(Long itemId) {
        List<Inventory> inventories = inventoryRepository.findAllByItem_ItemId(itemId);
        return inventories.stream()
                .map(this::convertToDto)
                .toList();
    }
    public List<InventoryDto> getAllInventoryByWarehouse(long warehouseId) {
        List<Inventory> inventories = inventoryRepository.findAllByWarehouse_WarehouseId(warehouseId);
        return inventories.stream()
                .map(this::convertToDto)
                .toList();
    }
    
    public InventoryDto getInventoryById(long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        return convertToDto(inventory);
    }
    
    public InventoryDto updateInventory(Long inventoryId, Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findById(inventoryId).orElse(null);
        if (existingInventory != null) {
            if(inventory.getQuantity() < inventory.getOnDemandQuantity()){
                throw new CustomException("Số lượng đã đặt không thể lớn hơn số lượng còn lại trong kho", HttpStatus.BAD_REQUEST);
            }
            existingInventory.setQuantity(inventory.getQuantity());
            existingInventory.setOnDemandQuantity(inventory.getOnDemandQuantity());
            Inventory updatedInventory = inventoryRepository.save(existingInventory);
            return convertToDto(updatedInventory);
        }
        return null;
    }

    //Tăng Quantity 
    public InventoryDto putItemToInventory(InventoryRequest inventoryRequest) {
        // Kiểm tra xem inventory đã tồn tại chưa (theo itemId và warehouseId)
        Inventory existingInventory = inventoryRepository.findByItem_ItemIdAndWarehouse_WarehouseId(
                inventoryRequest.getItemId(),
                inventoryRequest.getWarehouseId()
        );

        Inventory inventory;
        if (existingInventory != null) {
            existingInventory.setQuantity(existingInventory.getQuantity() + inventoryRequest.getQuantity());
            inventory = inventoryRepository.save(existingInventory);
        } else {
            inventory = new Inventory();
            Item item = itemRepository.findById(inventoryRequest.getItemId())
                    .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND));
            Warehouse warehouse = warehouseRepository.findById(inventoryRequest.getWarehouseId())
                    .orElseThrow(() -> new CustomException("Warehouse không tồn tại", HttpStatus.NOT_FOUND));
            inventory.setItem(item);  // Đảm bảo bạn có constructor hoặc setEntityId
            inventory.setWarehouse(warehouse);
            inventory.setQuantity(inventoryRequest.getQuantity());
            inventory.setOnDemandQuantity(0.0); // hoặc 1 giá trị mặc định
            inventory = inventoryRepository.save(inventory);
        }

        return convertToDto(inventory);
    }
    // Tăng onDemandQuantity nếu còn đủ hàng tồn
    public InventoryDto increaseOnDemand(Long inventoryId, Double n) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new CustomException("Không tìm thấy Inventory!", HttpStatus.NOT_FOUND));

        double availableQuantity = inventory.getQuantity() - inventory.getOnDemandQuantity();

        if (n > availableQuantity) {
            throw new CustomException("Không đủ hàng tồn để đặt trước!", HttpStatus.BAD_REQUEST);
        }

        inventory.setOnDemandQuantity(inventory.getOnDemandQuantity() + n);
        Inventory updated = inventoryRepository.save(inventory);
        return convertToDto(updated);
    }

    // Giảm cả onDemand và Quantity
    public InventoryDto consumeOnDemand(Long inventoryId, Double n) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new CustomException("Không tìm thấy Inventory!", HttpStatus.NOT_FOUND));

        if (n > inventory.getOnDemandQuantity()) {
            throw new CustomException("Không thể giảm quá số lượng đã đặt!", HttpStatus.BAD_REQUEST);
        }

        inventory.setOnDemandQuantity(inventory.getOnDemandQuantity() - n);
        inventory.setQuantity(inventory.getQuantity() - n);

        Inventory updated = inventoryRepository.save(inventory);
        return convertToDto(updated);
    }


    public InventoryDto convertToDto(Inventory inventory){
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setInventoryId(inventory.getInventoryId());
        inventoryDto.setWarehouseId(inventory.getWarehouse().getWarehouseId());
        inventoryDto.setItemId(inventory.getItem().getItemId());
        inventoryDto.setQuantity(inventory.getQuantity());
        inventoryDto.setOnDemandQuantity(inventory.getOnDemandQuantity());
        return inventoryDto;
    }
}
