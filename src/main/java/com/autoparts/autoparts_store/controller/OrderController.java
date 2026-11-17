package com.autoparts.autoparts_store.controller;

// Импортируем DTO для передачи информации о заказах.
import com.autoparts.autoparts_store.dto.OrderResponse;

// Импортируем сущность пользователя.
import com.autoparts.autoparts_store.dto.UpdateOrderStatusRequest;
import com.autoparts.autoparts_store.entity.User;

// Импортируем сервис, в котором находится вся бизнес-логика заказов.
import com.autoparts.autoparts_store.service.OrderService;
import com.autoparts.autoparts_store.service.UserService;

// Импортируем классы Spring Security для получения данных авторизованного пользователя.
import org.springframework.security.core.Authentication;

// Импортируем аннотации Spring для создания REST-контроллера и обработки запросов.
import org.springframework.web.bind.annotation.*;

// Импортируем List для возврата списка заказов.
import java.util.List;

// Указываем, что класс является REST-контроллером.
// Он принимает HTTP-запросы и возвращает данные в формате JSON.
@RestController

// Все запросы этого контроллера начинаются с /orders.
@RequestMapping("/orders")
public class OrderController {

    // Сервис для выполнения операций с заказами.
    private final OrderService orderService;

    // Сервис для получения данных пользователя.
    private final UserService userService;

    // Конструктор для внедрения необходимых сервисов.
    public OrderController(
            OrderService orderService,
            UserService userService) {

        this.orderService = orderService;
        this.userService = userService;
    }

    // POST /orders
    // Оформляет новый заказ из товаров, находящихся в корзине пользователя.
    @PostMapping
    public OrderResponse createOrder(Authentication authentication) {

        // Получаем пользователя, который выполнил вход в систему.
        User user = userService.findByEmail(authentication.getName());

        // Передаём оформление заказа сервису.
        return orderService.createOrder(user);
    }

    // GET /orders
    // Возвращает список заказов текущего пользователя.
    @GetMapping
    public List<OrderResponse> getUserOrders(Authentication authentication) {

        // Получаем данные авторизованного пользователя.
        User user = userService.findByEmail(authentication.getName());

        // Вызываем сервис для получения списка его заказов.
        return orderService.getUserOrders(user);
    }

    // GET /orders/{id}
    // Возвращает информацию об одном заказе.
    @GetMapping("/{id}")
    public OrderResponse getOrderById(
            Authentication authentication,
            @PathVariable Long id) {

        // Получаем данные авторизованного пользователя.
        User user = userService.findByEmail(authentication.getName());

        // Передаём идентификатор заказа и пользователя в сервис.
        return orderService.getOrderById(user, id);
    }

    // GET /orders/all
    // Возвращает все заказы покупателей для сотрудника или администратора.
    @GetMapping("/all")
    public List<OrderResponse> getAllOrders() {

        // Получаем все заказы через сервис.
        return orderService.getAllOrders();
    }

    // PATCH /orders/{id}/status
    // Изменяет статус заказа.
    @PatchMapping("/{id}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request) {

        // Передаём идентификатор заказа и новый статус в сервис.
        return orderService.updateOrderStatus(id, request.status());
    }

}