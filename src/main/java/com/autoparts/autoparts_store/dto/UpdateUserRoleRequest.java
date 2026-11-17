package com.autoparts.autoparts_store.dto;

import com.autoparts.autoparts_store.entity.UserRole;

// DTO для получения новой роли пользователя.
// Администратор указывает роль, которую необходимо назначить.
public record UpdateUserRoleRequest(

        // Новая роль пользователя.
        UserRole role

) {}