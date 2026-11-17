package com.autoparts.autoparts_store.repository;

// Импортируем сущность товара внутри заказа.
import com.autoparts.autoparts_store.entity.OrderItem;

// Импортируем JpaRepository для работы с базой данных.
import org.springframework.data.jpa.repository.JpaRepository;

// Импортируем List для получения списка товаров.
import java.util.List;

// Репозиторий отвечает за работу с товарами,
// которые входят в состав заказов.
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Находит все товары, относящиеся к определённому заказу.
    // orderId — идентификатор заказа.
    List<OrderItem> findByOrderId(Long orderId);
}