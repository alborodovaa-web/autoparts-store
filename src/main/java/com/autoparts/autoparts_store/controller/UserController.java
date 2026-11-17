
package com.autoparts.autoparts_store.controller;

import com.autoparts.autoparts_store.dto.*;
import com.autoparts.autoparts_store.entity.User;
import com.autoparts.autoparts_store.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с пользователями.
 * Принимает HTTP-запросы, вызывает сервис и возвращает ответы.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    // Сервис для выполнения операций с пользователями.
    private final UserService userService;

    // Менеджер аутентификации для проверки логина и пароля.
    private final AuthenticationManager authenticationManager;

    // Конструктор для внедрения необходимых зависимостей.
    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // POST /users/register
    // Регистрируем нового пользователя.
    // @Valid запускает проверки полей RegisterRequest.
    @PostMapping("/register")
    public UserResponse registerUser(
            @Valid @RequestBody RegisterRequest request) {

        // Создаём пользователя из регистрационных данных.
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        // Передаём пользователя в сервис для регистрации.
        User savedUser = userService.registerUser(user);

        // Возвращаем данные пользователя без пароля.
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    // POST /users/login
    // Выполняем вход пользователя в систему.
    @PostMapping("/login")
    public UserResponse loginUser(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        // Проверяем электронную почту и пароль пользователя.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Создаём новый контекст безопасности.
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Сохраняем данные авторизованного пользователя.
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // Создаём HTTP-сессию для пользователя.
        httpRequest.getSession(true);

        // Сохраняем контекст безопасности в HTTP-сессии.
        new HttpSessionSecurityContextRepository()
                .saveContext(context, httpRequest, httpResponse);

        // Получаем данные пользователя из базы данных.
        User user = userService.findByEmail(request.email());

        // Возвращаем информацию о пользователе без пароля.
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    // GET /users/me
    // Получаем данные текущего авторизованного пользователя.
    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {

        // Находим пользователя по электронной почте из авторизации.
        User user = userService.findByEmail(authentication.getName());

        // Возвращаем данные пользователя без пароля.
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    // PATCH /users/me
    // Обновляем данные текущего пользователя.
    @PatchMapping("/me")
    public UserResponse updateCurrentUser(
            Authentication authentication,
            @RequestBody UpdateUserRequest request) {

        // Передаём новые данные в сервис.
        User updatedUser = userService.updateUser(
                authentication.getName(),
                request.name(),
                request.email()
        );

        // Возвращаем обновлённые данные пользователя.
        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getRole(),
                updatedUser.getCreatedAt()
        );
    }

    // POST /users/employees
    // Создаём нового сотрудника.
    // Доступ разрешён только администратору.
    @PostMapping("/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createEmployee(
            @RequestBody CreateEmployeeRequest request) {

        // Передаём данные сотрудника в сервис.
        User employee = userService.createEmployee(
                request.name(),
                request.email(),
                request.password()
        );

        // Возвращаем данные сотрудника без пароля.
        return new UserResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getRole(),
                employee.getCreatedAt()
        );
    }

    // GET /users/all
    // Получаем список всех пользователей.
    // Доступ разрешён только администратору.
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserListResponse> getAllUsers() {

        // Получаем список пользователей через сервис.
        return userService.getAllUsers();
    }

    // PATCH /users/{id}/role
    // Изменяем роль выбранного пользователя.
    // Доступ разрешён только администратору.
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest request,
            Authentication authentication) {

        // Передаём данные в сервис для изменения роли.
        User user = userService.updateUserRole(
                id,
                request.role(),
                authentication.getName()
        );

        // Возвращаем обновлённые данные пользователя.
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
