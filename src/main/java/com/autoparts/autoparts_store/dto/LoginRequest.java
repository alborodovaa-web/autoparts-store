package com.autoparts.autoparts_store.dto;

public record LoginRequest(
        String email,
        String password
) {
}