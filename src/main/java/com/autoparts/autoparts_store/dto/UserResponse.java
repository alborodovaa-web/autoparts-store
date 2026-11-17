package com.autoparts.autoparts_store.dto;

import com.autoparts.autoparts_store.entity.UserRole;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt
) {
}