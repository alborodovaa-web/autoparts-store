package com.autoparts.autoparts_store.repository;

import com.autoparts.autoparts_store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

}