package de.tuda.prg.parser.generalvisitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import de.tuda.prg.annotations.Enclave;

import java.util.ArrayList;

public class ClassNameGetterVisitor extends VoidVisitorAdapter<String[]> {

    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, String[] className) {  // Only works when you have only one class definition in the file
        className[0] = cOrID.getNameAsString();
        // super.visit(cOrID, classNameSet); // TODO : is this needed ?
    }
}
