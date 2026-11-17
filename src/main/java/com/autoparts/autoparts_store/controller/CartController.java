
package com.autoparts.autoparts_store.controller;

import com.autoparts.autoparts_store.dto.CartItemRequest;
import com.autoparts.autoparts_store.dto.CartItemResponse;
import com.autoparts.autoparts_store.dto.UpdateCartItemRequest;
import com.autoparts.autoparts_store.entity.User;
import com.autoparts.autoparts_store.service.CartService;
import com.autoparts.autoparts_store.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов,
 * связанных с корзиной покупателя.
 *
 * Контроллер получает данные запроса,
 * вызывает сервис и возвращает результат.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    // Сервис для работы с корзиной.
    private final CartService cartService;

    // Сервис для получения данных пользователя.
    private final UserService userService;

    // Конструктор для внедрения сервисов.
    public CartController(
            CartService cartService,
            UserService userService) {

        this.cartService = cartService;
        this.userService = userService;
    }

    // Получаем содержимое корзины текущего пользователя.
    @GetMapping
    public List<CartItemResponse> getCart(Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        return cartService.getCartItems(user);
    }

    // Добавляем товар в корзину.
    @PostMapping("/items")
    public CartItemResponse addProduct(
            Authentication authentication,
            @RequestBody CartItemRequest request) {

        User user = userService.findByEmail(authentication.getName());

        return cartService.addProduct(
                user,
                request.productId(),
                request.quantity()
        );
    }

    // Изменяем количество товара в корзине.
    @PatchMapping("/items/{id}")
    public CartItemResponse updateQuantity(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody UpdateCartItemRequest request) {

        User user = userService.findByEmail(authentication.getName());

        return cartService.updateQuantity(
                user,
                id,
                request.quantity()
        );
    }

    // Удаляем товар из корзины.
    @DeleteMapping("/items/{id}")
    public void removeProduct(
            Authentication authentication,
            @PathVariable Long id) {

        User user = userService.findByEmail(authentication.getName());

        cartService.removeProduct(user, id);
    }
}
