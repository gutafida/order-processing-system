package com.gutafida.orderservice.client.inventory.dto;

public record ReserveInventoryResponse(Long productId,
        Integer quantity,
        Integer reservedQuantity,
        Boolean reserved,
        String message) {

}