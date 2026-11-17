
package com.autoparts.autoparts_store.config;

// Сервис, который загружает данные пользователя из базы данных.
import com.autoparts.autoparts_store.service.CustomUserDetailsService;

// Импорты для настройки Spring и Spring Security.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Указываем, что класс содержит настройки приложения.
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Создаём провайдер, который проверяет данные пользователя при входе.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        // Получаем пользователей из нашей базы данных.
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        // Указываем, что пароли проверяются с помощью BCrypt.
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // Получаем менеджер аутентификации Spring Security.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // Настраиваем правила безопасности для HTTP-запросов.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider) throws Exception {

        http
                // Отключаем CSRF-защиту для текущей настройки API.
                .csrf(csrf -> csrf.disable())

                // Подключаем наш провайдер проверки пользователей.
                .authenticationProvider(authenticationProvider)

                // Настраиваем доступ к адресам приложения.
                .authorizeHttpRequests(auth -> auth

                        // Регистрация и вход доступны без авторизации.
                        .requestMatchers(
                                "/users/register",
                                "/users/login"
                        ).permitAll()

                        // Разрешаем всем просматривать товары, категории и бренды.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/products",
                                "/products/*",
                                "/products/search",
                                "/products/category/*",
                                "/products/brand/*",
                                "/categories",
                                "/categories/*",
                                "/brands",
                                "/brands/*"
                        ).permitAll()

                        // Только администратор может добавлять товары.
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")

                        // Только администратор может изменять товары.
                        .requestMatchers(HttpMethod.PATCH, "/products/*").hasRole("ADMIN")

                        // Только администратор может удалять товары.
                        .requestMatchers(HttpMethod.DELETE, "/products/*").hasRole("ADMIN")

                        // Только администратор может создавать категории.
                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")

                        // Только администратор может массово создавать категории.
                        .requestMatchers(HttpMethod.POST, "/categories/spisok").hasRole("ADMIN")

                        // Только администратор может создавать бренды.
                        .requestMatchers(HttpMethod.POST, "/brands").hasRole("ADMIN")

                        // Только администратор может массово создавать бренды.
                        .requestMatchers(HttpMethod.POST, "/brands/spisok").hasRole("ADMIN")

                        // Просматривать все заказы могут сотрудники и администраторы.
                        .requestMatchers(HttpMethod.GET, "/orders/all")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Изменять статус заказа могут сотрудники и администраторы.
                        .requestMatchers(HttpMethod.PATCH, "/orders/*/status")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Создавать сотрудников может только администратор.
                        .requestMatchers(HttpMethod.POST, "/users/employees")
                        .hasRole("ADMIN")

                        // Просматривать всех пользователей может только администратор.
                        .requestMatchers(HttpMethod.GET, "/users/all")
                        .hasRole("ADMIN")

                        // Изменять роли пользователей может только администратор.
                        .requestMatchers(HttpMethod.PATCH, "/users/*/role")
                        .hasRole("ADMIN")

                        // Все остальные запросы доступны только авторизованным пользователям.
                        .anyRequest().authenticated()
                )

                // Отключаем стандартную форму входа Spring Security.
                .formLogin(form -> form.disable())

                // Отключаем HTTP Basic-аутентификацию.
                .httpBasic(basic -> basic.disable());

        // Собираем и возвращаем настроенную цепочку безопасности.
        return http.build();
    }
}
