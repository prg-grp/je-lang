package de.tuda.prg.exceptions;

public class TranslationException extends RuntimeException {

    String exceptionMessage;

    public TranslationException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
