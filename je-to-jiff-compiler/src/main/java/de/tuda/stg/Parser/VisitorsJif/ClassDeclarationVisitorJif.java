package de.tuda.stg.Parser.VisitorsJif;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.stg.Annotations.Enclave;
import de.tuda.stg.Parser.Codes;

import java.util.HashSet;

public class ClassDeclarationVisitorJif extends VoidVisitorAdapter<HashSet<String>>  {
    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, HashSet<String> classNameSet) {
        if (cOrID.isAnnotationPresent(Enclave.class)) {
            classNameSet.add(cOrID.getNameAsString());
            cOrID.getAnnotationByClass(Enclave.class).get().remove();   // Removing the @Enclave annotation
            cOrID.setName(cOrID.getName()+ Codes.classExtensionCode);
            cOrID.addModifier(Modifier.Keyword.FINAL);   // Adding the keyword 'final'
            super.visit(cOrID, classNameSet);
        }
    }
}
