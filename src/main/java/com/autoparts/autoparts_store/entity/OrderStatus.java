package com.autoparts.autoparts_store.entity;

// Возможные статусы заказа в магазине автозапчастей.
public enum OrderStatus {

    // Заказ создан, но ещё не обработан сотрудником.
    PENDING,

    // Заказ принят в обработку.
    PROCESSING,

    // Заказ отправлен покупателю.
    SHIPPED,

    // Заказ доставлен покупателю.
    DELIVERED,

    // Заказ отменён.
    CANCELLED
}