
package com.autoparts.autoparts_store.repository;

import com.autoparts.autoparts_store.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторий для работы с товарами в базе данных.
 *
 * Позволяет получать, сохранять, изменять и удалять товары.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Универсальный поиск товаров.
     *
     * Каждый параметр необязательный:
     * search — поиск по названию;
     * categoryId — фильтрация по категории;
     * brandId — фильтрация по бренду.
     *
     * Если параметр не передан, соответствующий фильтр не применяется.
     */
    @Query("""
            SELECT p FROM Product p
            WHERE (:search IS NULL OR
                   LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:categoryId IS NULL OR p.category.id = :categoryId)
            AND (:brandId IS NULL OR p.brand.id = :brandId)
            """)
    List<Product> findProducts(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId
    );
}
