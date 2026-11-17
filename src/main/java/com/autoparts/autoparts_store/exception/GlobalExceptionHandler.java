
package com.autoparts.autoparts_store.exception;

// Импортируем собственное исключение для отсутствующих объектов.
import com.autoparts.autoparts_store.exception.ResourceNotFoundException;

// Импортируем классы Spring для обработки ошибок REST API.
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// Импортируем Map для формирования ответа с сообщением об ошибке.
import java.util.Map;

/**
 * Глобальный обработчик ошибок REST API.
 * Позволяет возвращать клиенту понятные сообщения вместо стандартных ошибок.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Обрабатываем ошибки, связанные с неправильными данными
    // или невозможностью выполнить операцию.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }

    // Обрабатываем ошибки проверки полей DTO.
    // Например, пустое имя или неправильный формат электронной почты.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        // Получаем первое сообщение об ошибке проверки.
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Проверьте правильность введённых данных");

        // Возвращаем статус 400 и сообщение об ошибке.
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", errorMessage));
    }

    // Обрабатываем ошибки чтения тела HTTP-запроса.
    // Например, неправильный JSON или неизвестное значение Enum.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRequestBody(
            HttpMessageNotReadableException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error",
                        "Некорректные данные в теле запроса"
                ));
    }

    // Обрабатываем запросы к несуществующим адресам API.
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundResource(
            NoResourceFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error",
                        "Запрашиваемый адрес не найден"
                ));
    }

    // Обрабатываем конфликты при сохранении данных в базе.
    // Например, нарушение уникальности электронной почты.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataConflict(
            DataIntegrityViolationException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error",
                        "Не удалось сохранить данные: конфликт с существующими записями"
                ));
    }

    // Обрабатываем ситуации, когда нужный объект не найден.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(
            ResourceNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", exception.getMessage()));
    }
}
