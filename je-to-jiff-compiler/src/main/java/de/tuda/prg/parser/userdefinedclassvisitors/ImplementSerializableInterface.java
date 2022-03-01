package de.tuda.prg.parser.userdefinedclassvisitors;

import java.io.Serializable;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.tuda.prg.constants.Codes;

public class ImplementSerializableInterface extends VoidVisitorAdapter<Void>  {

    @Override
    public void visit(ClassOrInterfaceDeclaration cOrID, Void arg) {
        cOrID.addImplementedType(Serializable.class);
        super.visit(cOrID, arg);
    }
}
