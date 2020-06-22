package de.tuda.stg.jsgx.checker.qual;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Specifies that it's a method-level annotation
public @interface gateway {
}
