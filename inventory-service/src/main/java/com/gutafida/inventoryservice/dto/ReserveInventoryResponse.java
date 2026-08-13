package com.gutafida.inventoryservice.dto;

public record ReserveInventoryResponse(

        Long productId,

        Integer quantity,

        Integer reservedQuantity,

        Boolean reserved,

        String message

) {
}