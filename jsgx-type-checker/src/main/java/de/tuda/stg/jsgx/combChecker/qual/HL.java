package de.tuda.stg.jsgx.combChecker.qual;

import org.checkerframework.framework.qual.SubtypeOf;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
// @DefaultQualifierInHierarchy
@SubtypeOf({})
public @interface HL {
}
