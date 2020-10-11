package de.tuda.stg.processor.jsgxAnnotationProcessor;

/* This class stores a collection of classes annotated with the @SGX annotation.
 * During the annotation processing phase, an instance of this class is created when processing a class with @SGX annotation,
 * to hold the necessary meta-data about it. */

import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SGXGroupedClasses {

    private String qualifiedClassName;

    private Map<String, SGXAnnotatedClass> annotatedClassMap = new LinkedHashMap<String, SGXAnnotatedClass>();

    public SGXGroupedClasses(String qualifiedClassName) {
        this.qualifiedClassName = qualifiedClassName;
    }

    /*public void add(SGXAnnotatedClass toInsert)  {

        SGXAnnotatedClass existing = annotatedClassMap.get(toInsert.getId());
        if (existing != null) {
            throw new IdAlreadyUsedException(existing);
        }

        itemsMap.put(toInsert.getId(), toInsert);
    }
    public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(qualifiedClassName);
        String factoryClassName = superClassName.getSimpleName() + SUFFIX;

        // Creating Jiff class
        String className = "get the name of the SGX annotated class";
        TypeSpec jiffClass = TypeSpec.classBuilder(className).addModifiers()




    }*/

}
