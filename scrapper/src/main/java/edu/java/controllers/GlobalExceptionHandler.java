package edu.java.controllers;

import edu.java.dto.ApiErrorResponse;
import edu.java.exceptions.DataBaseSaveException;
import edu.java.exceptions.DuplicateRegistrationException;
import edu.java.exceptions.DuplicateTgChatLinkBindException;
import edu.java.exceptions.RegistrationException;
import edu.java.exceptions.TgChatLinkBindNotFoundException;
import edu.java.exceptions.UndefinedTgChatIdException;
import edu.java.exceptions.UndefinedUrlException;
import edu.java.exceptions.UrlFormatException;
import java.util.Arrays;
import java.util.NoSuchElementException;
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
    public ApiErrorResponse handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex,
        WebRequest request
    ) {

        return new ApiErrorResponse(
            "Validation error\n" + request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrorResponse handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException ex,
        WebRequest request
    ) {
        return new ApiErrorResponse(
            "Http method is not allowed\n" + request.getDescription(true),
            HttpStatus.METHOD_NOT_ALLOWED.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(DuplicateRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleDuplicateRegistrationException(
        DuplicateRegistrationException ex,
        WebRequest request
    ) {

        return new ApiErrorResponse(
            "Re-registration is not available\n" + request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(DuplicateTgChatLinkBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleDuplicateLinkExceptions(DuplicateTgChatLinkBindException ex, WebRequest request) {

        return new ApiErrorResponse(
            "Duplicate links for a tg-chat-id are not available\n" + request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(UndefinedTgChatIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleUndefinedTgChatIdExceptions(UndefinedTgChatIdException ex, WebRequest request) {
        return new ApiErrorResponse(
            "Undefined tg-chat-id\n" + request.getDescription(true),
            HttpStatus.NOT_FOUND.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {

        return new ApiErrorResponse(
            request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(DataBaseSaveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleDataBaseSaveException(DataBaseSaveException ex, WebRequest request) {

        return new ApiErrorResponse(
            request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRegistrationException(RegistrationException ex, WebRequest request) {

        return new ApiErrorResponse(
            request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(TgChatLinkBindNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleTgChatLinkBindNotFoundException(
        TgChatLinkBindNotFoundException ex,
        WebRequest request
    ) {

        return new ApiErrorResponse(
            request.getDescription(true),
            HttpStatus.NOT_FOUND.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(UndefinedUrlException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleUndefinedUrlException(
        UndefinedUrlException ex,
        WebRequest request
    ) {

        return new ApiErrorResponse(
            request.getDescription(true),
            HttpStatus.NOT_FOUND.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }

    @ExceptionHandler(UrlFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleUrlFormatException(UrlFormatException ex, WebRequest request) {

        return new ApiErrorResponse(
            request.getDescription(true),
            HttpStatus.BAD_REQUEST.toString(),
            ex.getClass().getName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }







}
