package de.tuda.prg.parser.visitorsje.sgxChecker.tud.enums;

public enum AnnotationEnum {
    GATEWAY("@Gateway"),
    ENCLAVE("@Enclave"),
    SECRET("@Secret");

    private final String annotation;

    AnnotationEnum(String annotation) {
        this.annotation = annotation;
    }

    public String toString(){
        return this.annotation;
    }
}
