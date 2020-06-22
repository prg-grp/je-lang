package de.tuda.stg.processor.jsgxAnnotationProcessor;

/* This class stores some information about a class annotated with the @SGX annotation.
* During the annotation processing phase, an instance of this class is created when processing a class with @SGX annotation,
* to hold the necessary meta-data about it. */

import de.tuda.stg.processor.jsgxAnnotations.SGX;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class SGXAnnotatedClass {
    private TypeElement annotatedClassElement;
    private String qualifiedSuperClassName;
    private String simpleTypeName;
    // private String id; // not necessary

    public SGXAnnotatedClass (TypeElement classElement) throws IllegalArgumentException {
        this.annotatedClassElement = classElement;
        SGX annotation = classElement.getAnnotation(SGX.class);
        try {
           /* Class<?> clazz = annotation.type();
            qualifiedSuperClassName = clazz.getCanonicalName();
            simpleTypeName = clazz.getSimpleName();*/
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedSuperClassName = classTypeElement.getQualifiedName().toString();
            simpleTypeName = classTypeElement.getSimpleName().toString();
        }
    }


}
