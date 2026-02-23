package com.example.movieapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка 404 - фильм не найден
     */
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFound(MovieNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header("X-Error-Type", "movie_not_found")
                .body(new ErrorResponse(e.getMessage()));
    }

    /**
     * Обработка ошибок от внешнего API (4xx)
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException e) {
        String message;
        HttpStatus status;
        
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            message = "Ошибка авторизации внешнего API";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            message = "Слишком много запросов к внешнему API";
            status = HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            message = "Ошибка при обращении к внешнему API: " + e.getStatusCode();
            status = HttpStatus.BAD_GATEWAY;
        }
        
        return ResponseEntity
                .status(status)
                .header("X-Error-Type", "external_api_error")
                .body(new ErrorResponse(message, e.getResponseBodyAsString()));
    }

    /**
     * Обработка ошибок сервера внешнего API (5xx)
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerError(HttpServerErrorException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .header("X-Error-Type", "external_api_down")
                .body(new ErrorResponse("Внешний API временно недоступен"));
    }

    /**
     * Обработка ошибок соединения
     */
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccess(ResourceAccessException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("X-Error-Type", "connection_error")
                .body(new ErrorResponse("Не удалось подключиться к внешнему API"));
    }

    /**
     * Обработка всех остальных ошибок
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception e) {
        e.printStackTrace(); // для отладки
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("X-Error-Type", "internal_error")
                .body(new ErrorResponse("Внутренняя ошибка сервера: " + e.getMessage()));
    }
}