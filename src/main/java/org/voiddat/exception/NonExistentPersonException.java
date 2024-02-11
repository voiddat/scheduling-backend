package org.voiddat.exception;

public class NonExistentPersonException extends RuntimeException {
    public NonExistentPersonException(String description) {
        super(description);
    }
}
