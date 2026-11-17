package com.autoparts.autoparts_store.service;

// Импортируем сущности, с которыми будет работать сервис.
import com.autoparts.autoparts_store.entity.Order;
import com.autoparts.autoparts_store.entity.OrderItem;
import com.autoparts.autoparts_store.entity.OrderStatus;
import com.autoparts.autoparts_store.entity.Cart;
import com.autoparts.autoparts_store.entity.CartItem;
import com.autoparts.autoparts_store.entity.User;

// Импортируем репозитории для работы с базой данных.
import com.autoparts.autoparts_store.repository.OrderRepository;
import com.autoparts.autoparts_store.repository.OrderItemRepository;
import com.autoparts.autoparts_store.repository.CartRepository;
import com.autoparts.autoparts_store.repository.CartItemRepository;

// Импортируем DTO для формирования ответа клиенту.
import com.autoparts.autoparts_store.dto.OrderResponse;
import com.autoparts.autoparts_store.dto.OrderItemResponse;

// Аннотация для создания Spring-сервиса.
import org.springframework.stereotype.Service;

// Аннотация для выполнения нескольких операций в одной транзакции.
import org.springframework.transaction.annotation.Transactional;

// Импортируем классы для работы с ценами, датой и списками.
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Сервис содержит основную бизнес-логику оформления и получения заказов.
@Service
public class OrderService {

    // Репозиторий для сохранения и получения заказов.
    private final OrderRepository orderRepository;

    // Репозиторий для сохранения товаров внутри заказов.
    private final OrderItemRepository orderItemRepository;

    // Репозиторий для поиска корзины пользователя.
    private final CartRepository cartRepository;

    // Репозиторий для получения и удаления товаров из корзины.
    private final CartItemRepository cartItemRepository;

    // Конструктор получает необходимые репозитории через внедрение зависимостей.
    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // Оформляет заказ на основе товаров, которые находятся в корзине пользователя.
    // Транзакция гарантирует, что заказ и его товары сохранятся вместе,
    // а корзина очистится только при успешном завершении операции.
    @Transactional
    public OrderResponse createOrder(User user) {

        // Ищем корзину текущего пользователя.
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Корзина не найдена"));

        // Получаем все товары из корзины.
        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        // Нельзя оформить заказ с пустой корзиной.
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(	"Корзина пуста. Невозможно оформить заказ");
        }

        // Создаём новый заказ и указываем его основные данные.
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // Переменная для подсчёта общей стоимости заказа.
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Сохраняем заказ, чтобы получить его идентификатор из базы данных.
        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

        // Перебираем все товары из корзины.
        for (CartItem cartItem : cartItems) {

            // Создаём запись о товаре внутри нового заказа.
            OrderItem orderItem = new OrderItem();

            // Связываем товар с заказом.
            orderItem.setOrder(order);

            // Указываем сам товар из каталога.
            orderItem.setProduct(cartItem.getProduct());

            // Переносим количество товара из корзины в заказ.
            orderItem.setQuantity(cartItem.getQuantity());

            // Сохраняем цену товара на момент оформления заказа.
            BigDecimal unitPrice = cartItem.getProduct().getPrice();
            orderItem.setUnitPrice(unitPrice);

            // Рассчитываем стоимость данной позиции:
            // цена одной единицы товара умножается на количество.
            BigDecimal itemTotal = unitPrice.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );

            // Прибавляем стоимость позиции к общей стоимости заказа.
            totalPrice = totalPrice.add(itemTotal);

            // Сохраняем товар внутри заказа.
            orderItemRepository.save(orderItem);
        }

        // Обновляем общую стоимость заказа после подсчёта всех товаров.
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // После успешного оформления удаляем товары из корзины.
        cartItemRepository.deleteAll(cartItems);

        // Возвращаем информацию о созданном заказе.
        return getOrderById(user, order.getId());
    }

    // Возвращает все заказы конкретного пользователя.
    public List<OrderResponse> getUserOrders(User user) {

        // Находим заказы пользователя и преобразуем каждый заказ в DTO.
        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Возвращает все заказы всех покупателей.
    // Этот метод будет использоваться сотрудниками и администраторами.
    public List<OrderResponse> getAllOrders() {

        // Получаем все заказы из базы данных
        // и преобразуем каждый заказ в DTO.
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Изменяет статус существующего заказа.
    // Метод вызывается сотрудником или администратором.
    public OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus newStatus) {

        // Проверяем, что новый статус передан.
        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать статус заказа"
            );
        }

        // Находим заказ по идентификатору.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Заказ не найден"));

        // Устанавливаем новый статус заказа.
        order.setStatus(newStatus);

        // Сохраняем изменения в базе данных.
        orderRepository.save(order);

        // Возвращаем обновлённую информацию о заказе.
        return convertToResponse(order);
    }


    // Возвращает один заказ по его идентификатору.
    public OrderResponse getOrderById(User user, Long orderId) {

        // Ищем заказ в базе данных.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Заказ не найден"));

        // Проверяем, что заказ принадлежит текущему пользователю.
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Этот заказ не принадлежит текущему пользователю"
            );
        }

        // Преобразуем найденный заказ в DTO.
        return convertToResponse(order);
    }

    // Преобразует сущность заказа в объект для отправки клиенту.
    private OrderResponse convertToResponse(Order order) {

        // Получаем все товары, которые входят в заказ.
        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(order.getId());

        // Преобразуем каждую запись о товаре в DTO.
        List<OrderItemResponse> items = orderItems.stream()
                .map(item -> {

                    // Рассчитываем стоимость конкретной позиции.
                    BigDecimal itemTotal = item.getUnitPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    );

                    // Формируем объект с информацией о товаре.
                    return new OrderItemResponse(
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getUnitPrice(),
                            item.getQuantity(),
                            itemTotal
                    );
                })
                .toList();

        // Формируем и возвращаем полную информацию о заказе.
        return new OrderResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getTotalPrice(),
                order.getStatus(),
                items
        );
    }
}
