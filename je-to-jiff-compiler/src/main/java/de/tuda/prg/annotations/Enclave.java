package de.tuda.prg.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)   // Specifies that it's a class-level annotation
public @interface Enclave {}