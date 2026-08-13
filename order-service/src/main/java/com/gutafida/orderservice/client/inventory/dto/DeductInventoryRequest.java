package com.gutafida.orderservice.client.inventory.dto;

public record DeductInventoryRequest(
        Long productId,
        Integer quantity
) {
}