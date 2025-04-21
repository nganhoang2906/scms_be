package com.scms.scms_be.repository.Inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Inventory.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    List<Inventory> findAllByItem_ItemId(long itemId);

    List<Inventory> findAllByWarehouse_WarehouseId(long warehouseId);

    Inventory findByItem_ItemIdAndWarehouse_WarehouseId(Long itemId, Long warehouseId);

    List<Inventory> findAllByItem_ItemIdAndWarehouse_WarehouseId(long itemId, long warehouseId);

    List<Inventory> findByItem_ItemId(Long itemId);


}
