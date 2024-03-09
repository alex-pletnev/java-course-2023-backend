package edu.java.exceptions;

public class DuplicateLinkException extends RuntimeException {

    public DuplicateLinkException() {
    }

    public DuplicateLinkException(String message) {
        super(message);
    }
}
