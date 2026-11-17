
package com.autoparts.autoparts_store.controller;

import com.autoparts.autoparts_store.entity.Product;
import com.autoparts.autoparts_store.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов,
 * связанных с товарами магазина.
 *
 * Контроллер принимает запросы, вызывает методы сервиса
 * и возвращает результаты клиенту.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    // Сервис, в котором находится логика работы с товарами.
    private final ProductService productService;

    // Конструктор для внедрения ProductService.
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Получаем товары с возможностью поиска и фильтрации.
    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId) {

        return productService.getProducts(search, categoryId, brandId);
    }

    // Получаем товар по его ID.
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Создаём новый товар.
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Обновляем информацию о товаре.
    @PatchMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        return productService.updateProduct(id, product);
    }

    // Удаляем товар по его ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        // Возвращаем статус 204 (No Content).
        return ResponseEntity.noContent().build();
    }
}
