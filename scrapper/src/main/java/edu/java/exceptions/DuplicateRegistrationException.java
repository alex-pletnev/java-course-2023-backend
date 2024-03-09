package edu.java.exceptions;

public class DuplicateRegistrationException extends RuntimeException {

    public DuplicateRegistrationException() {
    }

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
