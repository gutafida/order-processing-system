package com.gutafida.orderservice.client.inventory.dto;

public record ReserveInventoryRequest(
    Long productId,
    Integer quantity
) {
}