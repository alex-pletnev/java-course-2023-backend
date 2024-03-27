package edu.java.controllers;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.DuplicateLinkException;
import edu.java.exceptions.DuplicateRegistrationException;
import edu.java.exceptions.UndefinedTgChatIdException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        return new ApiErrorResponse(
            "Validation error",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrorResponse handleMethodNotSupportedException(
        HttpRequestMethodNotSupportedException ex,
        WebRequest request
    ) {
        return new ApiErrorResponse(
            "Http method is not allowed",
            HttpStatus.METHOD_NOT_ALLOWED.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(DuplicateRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRegistrationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        return new ApiErrorResponse(
            "Re-registration is not available",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(DuplicateLinkException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleDuplicateLinkExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        return new ApiErrorResponse(
            "Duplicate links for a tg-chat-id are not available",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(UndefinedTgChatIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleUndefinedTgChatIdExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        return new ApiErrorResponse(
            "Undefined tg-chat-id",
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

}