package com.gutafida.orderservice.client.inventory;

import com.gutafida.orderservice.client.inventory.dto.DeductInventoryRequest;
import com.gutafida.orderservice.client.inventory.dto.ReleaseInventoryRequest;
import com.gutafida.orderservice.client.inventory.dto.ReserveInventoryRequest;
import com.gutafida.orderservice.client.inventory.dto.ReserveInventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "inventory-service",
        url = "${inventory.service.url}"
)

public interface InventoryClient {
    @PutMapping("/api/inventory/reserve")
    ReserveInventoryResponse reserveInventory(@RequestBody ReserveInventoryRequest request);

    @PutMapping("/api/inventory/deduct")
    void deductInventory(@RequestBody DeductInventoryRequest request);

    @PutMapping("/api/inventory/release")
    void releaseInventory(@RequestBody ReleaseInventoryRequest request);
}
