package com.gutafida.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInventoryRequest(
        @NotNull
        Long productId,

        @NotBlank
        String productName,

        @NotBlank
        String sku,

        @NotNull
        @Min(0)
        Integer quantity

) {
}
