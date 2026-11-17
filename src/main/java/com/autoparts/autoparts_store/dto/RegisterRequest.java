
package com.autoparts.autoparts_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для получения данных при регистрации пользователя.
 * Содержит только те поля, которые пользователь должен передать.
 */
public record RegisterRequest(

        // Имя обязательно для заполнения.
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(max = 100, message = "Имя не должно превышать 100 символов")
        String name,

        // Проверяем, что электронная почта имеет правильный формат.
        @NotBlank(message = "Электронная почта обязательна")
        @Email(message = "Некорректный формат электронной почты")
        String email,

        // Пароль должен содержать не менее 8 символов.
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 100,
                message = "Пароль должен содержать от 8 до 100 символов")
        String password

) {}
