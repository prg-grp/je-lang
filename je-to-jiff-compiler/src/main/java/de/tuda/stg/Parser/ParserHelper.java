package de.tuda.stg.Parser;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.lang.annotation.Annotation;
import java.util.*;

public class ParserHelper {

    public static Map<String, List<MethodDeclaration>> getMethodDeclarationWithAnnotations(ClassOrInterfaceDeclaration cd, Set<String> annotationSet) {
        Map<String, List<MethodDeclaration>> annotationMethodsMap = new HashMap<>();
        if (annotationSet != null && !annotationSet.isEmpty()) {
            for (MethodDeclaration md : cd.getMethods()) {
                NodeList<AnnotationExpr> annotations = md.getAnnotations();
                for (AnnotationExpr annotationExpr : annotations) {
                    String annotationName = annotationExpr.getNameAsString();
                    if (annotationSet.contains(annotationName)) {
                        List<MethodDeclaration> mdList = annotationMethodsMap.get(annotationName);
                        if (mdList == null) {
                            List newMdList = new ArrayList<MethodDeclaration>();
                            newMdList.add(md);
                            annotationMethodsMap.put(annotationName, newMdList);
                        } else {
                            mdList.add(md);
                        }
                    }
                }
            }
        }
        return annotationMethodsMap;
    }
}
