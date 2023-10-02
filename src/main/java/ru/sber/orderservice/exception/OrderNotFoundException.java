package ru.sber.orderservice.exception;

public class OrderNotFoundException extends RuntimeException {
    /**
     * Исключение для проверки наличия заказа
     *
     * @param message сообщение об ошибке
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
