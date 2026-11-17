
package com.autoparts.autoparts_store.service;

import com.autoparts.autoparts_store.entity.Brand;
import com.autoparts.autoparts_store.repository.BrandRepository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с брендами товаров.
 *
 * Содержит логику получения и сохранения брендов.
 */
@Service
public class BrandService {

    // Репозиторий для взаимодействия с таблицей брендов.
    private final BrandRepository brandRepository;

    // Конструктор для внедрения репозитория.
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // Получаем список всех брендов.
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    // Сохраняем один новый бренд.
    public Brand saveBrand(Brand brand) {

        // Проверяем, что бренд и его название переданы.
        if (brand == null || brand.getName() == null
                || brand.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Название бренда не должно быть пустым"
            );
        }

        // Проверяем, существует ли бренд с таким названием.
        if (brandRepository.existsByName(brand.getName())) {
            throw new IllegalArgumentException("Бренд уже существует");
        }

        // Сохраняем новый бренд.
        return brandRepository.save(brand);
    }

    // Сохраняем несколько брендов одновременно.
    public List<Brand> saveAllBrands(List<Brand> brands) {

        // Проверяем, что список передан.
        if (brands == null || brands.isEmpty()) {
            throw new IllegalArgumentException(
                    "Список брендов не должен быть пустым"
            );
        }

        // Оставляем только корректные бренды,
        // которых ещё нет в базе данных.
        List<Brand> newBrands = brands.stream()
                .filter(brand -> brand != null)
                .filter(brand -> brand.getName() != null)
                .filter(brand -> !brand.getName().isBlank())
                .filter(brand ->
                        !brandRepository.existsByName(brand.getName()))
                .toList();

        // Если новых брендов нет, возвращаем пустой список.
        if (newBrands.isEmpty()) {
            return List.of();
        }

        // Сохраняем новые бренды.
        return brandRepository.saveAll(newBrands);
    }
}
