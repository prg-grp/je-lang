package de.tuda.prg.exceptions;

public class TranslationException extends RuntimeException {

    String exceptionMessage;

    public TranslationException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String toString() {
        return "TranslationException{" +
                "exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
