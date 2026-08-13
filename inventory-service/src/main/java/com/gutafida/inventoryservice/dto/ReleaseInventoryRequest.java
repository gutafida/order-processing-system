package com.gutafida.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReleaseInventoryRequest(
        @NotNull
        Long productId,

        @NotNull
        @Min(1)
        Integer quantity
) {
}
