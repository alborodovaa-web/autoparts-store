package com.autoparts.autoparts_store.service;

import com.autoparts.autoparts_store.entity.Category;
import com.autoparts.autoparts_store.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    public List<Category> saveAllCategories(List<Category> categories) {
        List<Category> newCategories = categories.stream()
                .filter(category -> !categoryRepository.existsByName(category.getName()))
                .toList();

        return categoryRepository.saveAll(newCategories);
    }
}