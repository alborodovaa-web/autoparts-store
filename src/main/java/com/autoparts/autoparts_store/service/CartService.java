
package com.autoparts.autoparts_store.service;

import com.autoparts.autoparts_store.dto.CartItemResponse;
import com.autoparts.autoparts_store.entity.Cart;
import com.autoparts.autoparts_store.entity.CartItem;
import com.autoparts.autoparts_store.entity.Product;
import com.autoparts.autoparts_store.entity.User;
import com.autoparts.autoparts_store.repository.CartRepository;
import com.autoparts.autoparts_store.repository.CartItemRepository;
import com.autoparts.autoparts_store.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для работы с корзиной покупателя.
 *
 * Здесь находится логика создания корзины,
 * добавления товаров, изменения количества,
 * удаления товаров и формирования ответов.
 */
@Service
public class CartService {

    // Репозиторий для работы с корзинами.
    private final CartRepository cartRepository;

    // Репозиторий для работы с товарами в корзине.
    private final CartItemRepository cartItemRepository;

    // Репозиторий для поиска товаров.
    private final ProductRepository productRepository;

    // Конструктор для внедрения репозиториев.
    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    // Получаем существующую корзину пользователя или создаём новую.
    public Cart getOrCreateCart(User user) {

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);

                    return cartRepository.save(cart);
                });
    }

    // Получаем содержимое корзины в виде DTO.
    public List<CartItemResponse> getCartItems(User user) {

        Cart cart = getOrCreateCart(user);

        return cartItemRepository.findByCartId(cart.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Добавляем товар в корзину.
    public CartItemResponse addProduct(
            User user,
            Long productId,
            Integer quantity) {

        // Проверяем, что количество товара больше нуля.
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Количество товара должно быть больше нуля"
            );
        }

        // Получаем корзину пользователя.
        Cart cart = getOrCreateCart(user);

        // Находим товар в базе данных.
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Товар не найден"));

        // Проверяем, есть ли этот товар уже в корзине.
        // Если есть, увеличиваем его количество.
        // Если нет, создаём новую позицию.
        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);

                    return newItem;
                });

        // Увеличиваем количество товара.
        item.setQuantity(item.getQuantity() + quantity);

        // Сохраняем позицию и преобразуем её в DTO.
        CartItem savedItem = cartItemRepository.save(item);

        return convertToResponse(savedItem);
    }

    // Изменяем количество товара в корзине.
    public CartItemResponse updateQuantity(
            User user,
            Long itemId,
            Integer quantity) {

        // Проверяем корректность количества.
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Количество товара должно быть больше нуля"
            );
        }

        // Получаем корзину текущего пользователя.
        Cart cart = getOrCreateCart(user);

        // Ищем позицию корзины по ID.
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Товар не найден в корзине"
                        ));

        // Проверяем, принадлежит ли позиция корзине пользователя.
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException(
                    "Этот товар не принадлежит корзине текущего пользователя"
            );
        }

        // Устанавливаем новое количество.
        item.setQuantity(quantity);

        // Сохраняем изменения и возвращаем DTO.
        CartItem savedItem = cartItemRepository.save(item);

        return convertToResponse(savedItem);
    }

    // Удаляем товар из корзины.
    public void removeProduct(User user, Long itemId) {

        // Получаем корзину текущего пользователя.
        Cart cart = getOrCreateCart(user);

        // Ищем позицию корзины.
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Товар не найден в корзине"
                        ));

        // Проверяем принадлежность позиции пользователю.
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException(
                    "Этот товар не принадлежит корзине текущего пользователя"
            );
        }

        // Удаляем позицию из корзины.
        cartItemRepository.delete(item);
    }

    /**
     * Преобразуем сущность CartItem в DTO.
     *
     * Рассчитываем общую стоимость позиции:
     * цена одного товара умножается на количество.
     */
    private CartItemResponse convertToResponse(CartItem item) {

        BigDecimal totalPrice = item.getProduct()
                .getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                totalPrice
        );
    }
}
