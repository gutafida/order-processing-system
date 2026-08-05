package com.gutafida.inventoryservice.dto;

public record InventoryResponse(
        Long id,

        Long productId,

        String productName,

        String sku,

        Integer quantity,

        Integer reservedQuantity,

        Boolean available
) {
}
