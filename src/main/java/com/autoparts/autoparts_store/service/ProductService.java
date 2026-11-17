
package com.autoparts.autoparts_store.service;

// Импортируем сущность товара.
import com.autoparts.autoparts_store.entity.Product;

// Импортируем исключение для отсутствующих объектов.
import com.autoparts.autoparts_store.exception.ResourceNotFoundException;

// Импортируем репозиторий товаров.
import com.autoparts.autoparts_store.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с товарами магазина.
 *
 * Здесь находится бизнес-логика получения,
 * поиска, создания, изменения и удаления товаров.
 */
@Service
public class ProductService {

    // Репозиторий для обращения к базе данных.
    private final ProductRepository productRepository;

    // Конструктор для внедрения репозитория.
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Получаем товары с возможностью поиска и фильтрации.
     *
     * Все параметры необязательные.
     */
    public List<Product> getProducts(
            String search,
            Long categoryId,
            Long brandId) {

        // Убираем лишние пробелы в поисковой строке.
        if (search != null) {
            search = search.trim();

            // Пустую строку считаем отсутствием поискового запроса.
            if (search.isEmpty()) {
                search = null;
            }
        }

        // Передаём параметры в репозиторий для поиска в базе данных.
        return productRepository.findProducts(
                search,
                categoryId,
                brandId
        );
    }

    // Сохраняем новый товар в базе данных.
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Обновляем существующий товар.
    public Product updateProduct(Long id, Product updatedProduct) {

        // Ищем товар по ID.
        // Если товар отсутствует, выбрасываем исключение.
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Товар не найден"));

        // Обновляем название, если оно передано.
        if (updatedProduct.getName() != null) {
            product.setName(updatedProduct.getName());
        }

        // Обновляем описание, если оно передано.
        if (updatedProduct.getDescription() != null) {
            product.setDescription(updatedProduct.getDescription());
        }

        // Обновляем цену, если она передана.
        if (updatedProduct.getPrice() != null) {
            product.setPrice(updatedProduct.getPrice());
        }

        // Обновляем количество, если оно передано.
        if (updatedProduct.getQuantity() != null) {
            product.setQuantity(updatedProduct.getQuantity());
        }

        // Обновляем изображение, если ссылка передана.
        if (updatedProduct.getImageUrl() != null) {
            product.setImageUrl(updatedProduct.getImageUrl());
        }

        // Сохраняем обновлённый товар.
        return productRepository.save(product);
    }

    // Удаляем товар по ID.
    public void deleteProduct(Long id) {

        // Проверяем, существует ли товар.
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Товар не найден");
        }

        // Удаляем товар из базы данных.
        productRepository.deleteById(id);
    }

    // Получаем один товар по его ID.
    public Product getProductById(Long id) {

        // Ищем товар и выбрасываем исключение, если он отсутствует.
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Товар не найден"));
    }
}
