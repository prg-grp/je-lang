package de.tuda.prg.exceptions;

public class RMICodeAdditionException extends RuntimeException {
    String exceptionMessage;

    public RMICodeAdditionException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
