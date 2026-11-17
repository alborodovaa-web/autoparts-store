package com.autoparts.autoparts_store.dto;

import com.autoparts.autoparts_store.entity.UserRole;
import java.time.LocalDateTime;

// DTO для отображения информации о пользователе в общем списке.
// Пароль не передаётся, чтобы не раскрывать конфиденциальные данные.
public record UserListResponse(

        // Уникальный идентификатор пользователя.
        Long id,

        // Имя пользователя.
        String name,

        // Электронная почта пользователя.
        String email,

        // Роль пользователя: CLIENT, EMPLOYEE или ADMIN.
        UserRole role,

        // Дата создания аккаунта.
        LocalDateTime createdAt

) {}