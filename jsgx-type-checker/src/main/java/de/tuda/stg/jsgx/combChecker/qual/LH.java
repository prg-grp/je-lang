package de.tuda.stg.jsgx.combChecker.qual;

import org.checkerframework.framework.qual.*;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({LL.class, HH.class})
// @QualifierForLiterals({LiteralKind.STRING})
@QualifierForLiterals
@DefaultQualifierInHierarchy  //Keeping it default, as type checking will be performed only for the SGX classes.
@DefaultFor({TypeUseLocation.LOWER_BOUND})  // is it needed
public @interface LH {
}
