package de.tuda.stg.Exceptions;

public class TranslationException extends RuntimeException {

    String exceptionMessage;

    public TranslationException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
