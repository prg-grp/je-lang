package de.tuda.stg.jsgx.checker.qual;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)   // Specifies that it's a class-level annotation
public @interface SGX {

}
