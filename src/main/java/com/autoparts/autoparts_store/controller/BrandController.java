
package com.autoparts.autoparts_store.controller;

import com.autoparts.autoparts_store.entity.Brand;
import com.autoparts.autoparts_store.service.BrandService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов,
 * связанных с брендами товаров.
 */
@RestController
@RequestMapping("/brands")
public class BrandController {

    // Сервис, в котором находится логика работы с брендами.
    private final BrandService brandService;

    // Конструктор для внедрения BrandService.
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Получаем список всех брендов.
    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    // Создаём один новый бренд.
    @PostMapping
    public Brand createBrand(@RequestBody Brand brand) {
        return brandService.saveBrand(brand);
    }

    // Создаём несколько брендов одновременно.
    @PostMapping("/spisok")
    public List<Brand> createBrands(
            @RequestBody List<Brand> brands) {

        return brandService.saveAllBrands(brands);
    }
}
