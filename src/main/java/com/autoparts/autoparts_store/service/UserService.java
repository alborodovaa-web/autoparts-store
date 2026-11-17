package com.autoparts.autoparts_store.service;

import com.autoparts.autoparts_store.dto.UserListResponse;
import com.autoparts.autoparts_store.entity.User;
import com.autoparts.autoparts_store.entity.UserRole;
import com.autoparts.autoparts_store.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с такой электронной почтой уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.CLIENT);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
    // Создаёт аккаунт нового сотрудника.
// Этот метод будет вызываться только администратором.
    public User createEmployee(
            String name,
            String email,
            String password) {

        // Проверяем, существует ли пользователь с такой почтой.
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Пользователь с такой электронной почтой уже существует"
            );
        }

        // Создаём нового пользователя.
        User employee = new User();

        // Заполняем основные данные сотрудника.
        employee.setName(name);
        employee.setEmail(email);

        // Хешируем пароль перед сохранением в базу данных.
        employee.setPassword(passwordEncoder.encode(password));

        // Назначаем роль сотрудника.
        employee.setRole(UserRole.EMPLOYEE);

        // Сохраняем дату создания аккаунта.
        employee.setCreatedAt(LocalDateTime.now());

        // Сохраняем сотрудника в базе данных.
        return userRepository.save(employee);
    }
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid email or password"
                ));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пользователь не найден"));
    }
    public User updateUser(String currentEmail, String newName, String newEmail) {

        User user = findByEmail(currentEmail);

        if (!user.getEmail().equals(newEmail)
                && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setName(newName);
        user.setEmail(newEmail);

        return userRepository.save(user);
    }
    // Возвращает список всех зарегистрированных пользователей.
// Этот метод будет использоваться администратором.
    public List<UserListResponse> getAllUsers() {

        // Получаем всех пользователей из базы данных.
        return userRepository.findAll()

                // Преобразуем каждую сущность User в безопасный DTO.
                .stream()
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))

                // Собираем преобразованные данные в список.
                .collect(Collectors.toList());
    }
    // Изменяет роль существующего пользователя.
// Метод вызывается администратором.
    public User updateUserRole(
            Long userId,
            UserRole newRole,
            String currentAdminEmail) {

        // Проверяем, что новая роль указана.
        if (newRole == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать новую роль"
            );
        }

        // Находим пользователя по идентификатору.
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пользователь не найден"));

        // Запрещаем администратору изменять собственную роль.
        // Это защищает от случайной потери административного доступа.
        if (user.getEmail().equals(currentAdminEmail)) {
            throw new IllegalArgumentException(
                    "Нельзя изменять собственную роль"
            );
        }

        // Устанавливаем новую роль.
        user.setRole(newRole);

        // Сохраняем изменения в базе данных.
        return userRepository.save(user);
    }
}