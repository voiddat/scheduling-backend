package org.voiddat.exception;

public class UnavailableTimeslotException extends RuntimeException {
    public UnavailableTimeslotException(String description) {
        super(description);
    }
}
