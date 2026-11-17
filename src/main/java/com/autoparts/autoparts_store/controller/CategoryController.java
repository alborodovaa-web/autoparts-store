
package com.autoparts.autoparts_store.controller;

import com.autoparts.autoparts_store.entity.Category;
import com.autoparts.autoparts_store.service.CategoryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов,
 * связанных с категориями товаров.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {

    // Сервис, в котором находится логика работы с категориями.
    private final CategoryService categoryService;

    // Конструктор для внедрения CategoryService.
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Получаем список всех категорий.
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Создаём одну новую категорию.
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // Создаём несколько категорий одновременно.
    @PostMapping("/spisok")
    public List<Category> createCategories(
            @RequestBody List<Category> categories) {

        return categoryService.saveAllCategories(categories);
    }
}
