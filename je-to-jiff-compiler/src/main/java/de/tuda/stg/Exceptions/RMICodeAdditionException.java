package de.tuda.stg.Exceptions;

public class RMICodeAdditionException extends RuntimeException {
    String exceptionMessage;

    public RMICodeAdditionException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
