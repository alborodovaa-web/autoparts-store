package com.autoparts.autoparts_store.repository;

// Импортируем сущность заказа.
import com.autoparts.autoparts_store.entity.Order;

// Импортируем JpaRepository для работы с базой данных.
import org.springframework.data.jpa.repository.JpaRepository;

// Импортируем List для получения списка заказов.
import java.util.List;

// Репозиторий отвечает за получение, сохранение,
// изменение и удаление заказов в базе данных.
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Находит все заказы конкретного пользователя.
    // userId — идентификатор пользователя, чьи заказы нужно получить.
    List<Order> findByUserId(Long userId);
}