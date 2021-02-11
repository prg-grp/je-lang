package de.tuda.prg.exceptions;

public class FileIOException extends Exception {
    String fileName;
    String message;

    public FileIOException(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }
}
