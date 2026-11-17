package com.autoparts.autoparts_store.dto;

import com.autoparts.autoparts_store.entity.OrderStatus;

// DTO для получения нового статуса заказа из HTTP-запроса.
// Сотрудник передаёт статус, который необходимо установить.
public record UpdateOrderStatusRequest(

        // Новый статус заказа.
        OrderStatus status

) {
}