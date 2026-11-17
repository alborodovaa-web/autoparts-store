package com.autoparts.autoparts_store.dto;

// Импортируем BigDecimal для точного хранения денежных значений.
import java.math.BigDecimal;

// DTO для передачи информации об одном товаре внутри заказа.
// Этот объект не хранится в базе данных, а используется для ответа клиенту.
public record OrderItemResponse(

        // Уникальный идентификатор товара в каталоге.
        Long productId,

        // Название товара.
        String productName,

        // Цена одной единицы товара на момент оформления заказа.
        BigDecimal unitPrice,

        // Количество заказанных единиц товара.
        Integer quantity,

        // Общая стоимость этой позиции: цена × количество.
        BigDecimal totalPrice

) {
}