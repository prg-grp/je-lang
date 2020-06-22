package de.tuda.stg.jsgx.combChecker.qual;

import org.checkerframework.framework.qual.*;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({HL.class})
// @QualifierForLiterals({LiteralKind.STRING})
// @QualifierForLiterals
// @DefaultQualifierInHierarchy
// @DefaultFor({TypeUseLocation.LOWER_BOUND})  // is it needed
public @interface LL {
}
