package com.autoparts.autoparts_store.dto;

public record UpdateUserRequest(
        String name,
        String email
) {
}