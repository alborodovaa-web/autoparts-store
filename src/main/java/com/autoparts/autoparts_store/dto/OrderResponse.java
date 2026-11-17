package com.autoparts.autoparts_store.dto;

// Импортируем BigDecimal для хранения денежных значений.
import java.math.BigDecimal;

// Импортируем LocalDateTime для хранения даты и времени заказа.
import java.time.LocalDateTime;

// Импортируем List для передачи списка товаров в заказе.
import java.util.List;

// Импортируем перечисление со статусами заказа.
import com.autoparts.autoparts_store.entity.OrderStatus;

// DTO для передачи полной информации о заказе клиенту.
// Используется при просмотре списка заказов и отдельного заказа.
public record OrderResponse(

        // Уникальный номер заказа.
        Long id,

        // Дата и время оформления заказа.
        LocalDateTime createdAt,

        // Общая стоимость всех товаров в заказе.
        BigDecimal totalPrice,

        // Текущий статус заказа.
        OrderStatus status,

        // Список товаров, которые входят в этот заказ.
        List<OrderItemResponse> items

) {
}