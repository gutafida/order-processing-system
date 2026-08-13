package com.gutafida.inventoryservice.controller;

import com.gutafida.inventoryservice.dto.*;
import com.gutafida.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody CreateInventoryRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.createInventory(request));

    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable Long productId){
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
    }
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory(){
       return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PutMapping("/reserve")
    public ResponseEntity<ReserveInventoryResponse> reserveInventory(@Valid @RequestBody ReserveInventoryRequest request){
        return ResponseEntity.ok(inventoryService.reserveInventory(request));

    }

    @PutMapping("/deduct")
    public ResponseEntity<InventoryResponse> deductInventory(@Valid @RequestBody DeductInventoryRequest request){
        return ResponseEntity.ok(inventoryService.deductInventory(request));
    }

    @PutMapping("/release")
    public ResponseEntity<InventoryResponse> releaseInventory(
            @Valid @RequestBody ReleaseInventoryRequest request
    ) {
        return ResponseEntity.ok(
                inventoryService.releaseInventory(request)
        );
    }

}
