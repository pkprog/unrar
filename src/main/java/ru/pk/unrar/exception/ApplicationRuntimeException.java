package ru.pk.unrar.exception;

public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(String message) {
        super(message);
    }
}
