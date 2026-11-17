
package com.autoparts.autoparts_store.exception;

/**
 * Исключение, которое возникает, когда запрашиваемый объект
 * не найден в базе данных.
 */
public class ResourceNotFoundException extends RuntimeException {

    // Конструктор принимает сообщение, объясняющее причину ошибки.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
