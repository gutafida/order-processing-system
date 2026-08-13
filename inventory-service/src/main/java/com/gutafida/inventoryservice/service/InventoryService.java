package com.gutafida.inventoryservice.service;

import com.gutafida.inventoryservice.dto.*;
import com.gutafida.inventoryservice.entity.Inventory;
import com.gutafida.inventoryservice.exception.ResourceNotFoundException;
import com.gutafida.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    public InventoryResponse createInventory(CreateInventoryRequest request){
        Inventory inventory = Inventory.builder()
                .productId(request.productId())
                .productName(request.productName())
                .sku(request.sku())
                .quantity(request.quantity())
                .reservedQuantity(0)
                .available(request.quantity()>0)
                .build();
        Inventory saved = inventoryRepository.save(inventory);
        return mapToResponse(saved);

    }

    public InventoryResponse getInventoryByProductId(Long productId){
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with product ID: " + productId));
        return mapToResponse(inventory);
    }
    public List<InventoryResponse> getAllInventory(){
        return inventoryRepository.findAll()
                .stream().map(this::mapToResponse)
                .toList();
    }

    public ReserveInventoryResponse reserveInventory(ReserveInventoryRequest request){
        Inventory inventory = inventoryRepository.findByProductId(request.productId())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Product not found with product ID: " + request.productId()));
        int available = inventory.getQuantity() - inventory.getReservedQuantity();
        if(available < request.quantity()){
            throw new IllegalStateException("Not enough inventory available");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() + request.quantity());
        inventory.setAvailable(inventory.getQuantity() > inventory.getReservedQuantity());
        inventoryRepository.save(inventory);
        return new ReserveInventoryResponse(
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                true,
                "Inventory reserved successfully"
        );

    }

    public InventoryResponse deductInventory(DeductInventoryRequest request){
        Inventory inventory = inventoryRepository.findByProductId(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with product ID: " + request.productId()));
        if(inventory.getReservedQuantity() < request.quantity()){
            throw new IllegalStateException("Not enough inventory reserved");
        }
        inventory.setQuantity(inventory.getQuantity() - request.quantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() - request.quantity());
        inventory.setAvailable(inventory.getQuantity() - inventory.getReservedQuantity() > 0);
        Inventory saved = inventoryRepository.save(inventory);
        return mapToResponse(saved);

    }


    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getProductName(),
                inventory.getSku(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                inventory.getAvailable()
        );
    }
}
