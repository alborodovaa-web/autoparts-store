package com.autoparts.autoparts_store.dto;

// DTO для получения данных нового сотрудника.
// Администратор передаёт имя, электронную почту и пароль.
public record CreateEmployeeRequest(

        // Имя нового сотрудника.
        String name,

        // Электронная почта сотрудника.
        String email,

        // Пароль для входа в аккаунт.
        String password

) {}