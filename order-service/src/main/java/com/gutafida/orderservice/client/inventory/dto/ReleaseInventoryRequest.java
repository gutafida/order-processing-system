package com.gutafida.orderservice.client.inventory.dto;

public record ReleaseInventoryRequest(
        Long productId,
        Integer quantity
) {
}
