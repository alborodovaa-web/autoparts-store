package com.autoparts.autoparts_store.repository;

import com.autoparts.autoparts_store.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

}