package edu.java.exceptions;

public class UndefinedTgChatIdException extends RuntimeException {

    public UndefinedTgChatIdException() {
    }

    public UndefinedTgChatIdException(String message) {
        super(message);
    }
}
