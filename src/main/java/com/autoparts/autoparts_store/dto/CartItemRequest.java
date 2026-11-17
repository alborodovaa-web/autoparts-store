package com.autoparts.autoparts_store.dto;

public record CartItemRequest(
        Long productId,
        Integer quantity
) {
}