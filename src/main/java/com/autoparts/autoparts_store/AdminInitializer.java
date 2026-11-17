
package com.autoparts.autoparts_store;

import com.autoparts.autoparts_store.entity.User;
import com.autoparts.autoparts_store.entity.UserRole;
import com.autoparts.autoparts_store.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

// Класс для первоначальной настройки администратора при запуске приложения.
@Configuration
public class AdminInitializer {

    // Создаём администратора, если пользователь с указанной почтой ещё не существует.
    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Получаем данные администратора из переменных окружения.
            String adminEmail = System.getenv("ADMIN_EMAIL");
            String adminPassword = System.getenv("ADMIN_PASSWORD");

            // Если данные не указаны, пропускаем создание администратора.
            if (adminEmail == null || adminEmail.isBlank()
                    || adminPassword == null || adminPassword.isBlank()) {
                System.out.println("Данные администратора не заданы. Создание пропущено.");
                return;
            }

            // Проверяем, существует ли администратор в базе данных.
            if (!userRepository.existsByEmail(adminEmail)) {

                // Создаём нового пользователя.
                User admin = new User();

                admin.setName("Администратор");
                admin.setEmail(adminEmail);

                // Перед сохранением шифруем пароль с помощью BCrypt.
                admin.setPassword(passwordEncoder.encode(adminPassword));

                // Назначаем пользователю роль администратора.
                admin.setRole(UserRole.ADMIN);

                // Записываем дату создания аккаунта.
                admin.setCreatedAt(LocalDateTime.now());

                // Сохраняем администратора в базе данных.
                userRepository.save(admin);

                System.out.println("Администратор успешно создан.");
            }
        };
    }
}
