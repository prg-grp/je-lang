package de.tuda.stg.Annotations;

import java.lang.annotation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
// @DefaultQualifierInHierarchy
// @SubtypeOf({})
public @interface Secret {
}