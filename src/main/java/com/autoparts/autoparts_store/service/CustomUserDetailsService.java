
package com.autoparts.autoparts_store.service;

import com.autoparts.autoparts_store.entity.User;
import com.autoparts.autoparts_store.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для поиска пользователей при авторизации.
 * Spring Security использует его, чтобы получить данные пользователя
 * и проверить правильность введённого пароля.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Репозиторий для получения пользователей из базы данных.
    private final UserRepository userRepository;

    // Конструктор для внедрения репозитория.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Находит пользователя по электронной почте.
     *
     * @param email электронная почта пользователя
     * @return данные пользователя для Spring Security
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Ищем пользователя в базе данных по электронной почте.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Пользователь с такой электронной почтой не найден"
                        )
                );

        // Передаём Spring Security электронную почту,
        // зашифрованный пароль и роль пользователя.
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())

                // Метод roles автоматически добавляет префикс ROLE_.
                // Например, ADMIN превращается в ROLE_ADMIN.
                .roles(user.getRole().name())

                // Создаём объект с данными пользователя.
                .build();
    }
}
