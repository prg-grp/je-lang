package de.tuda.prg.exceptions;

public class RMICodeAdditionException extends RuntimeException {
    String exceptionMessage;

    public RMICodeAdditionException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String toString() {
        return "RMICodeAdditionException{" +
                "exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
