package de.tuda.prg.parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import de.tuda.prg.parser.userdefinedclassvisitors.*;
import de.tuda.prg.parser.visitorsje.*;
import de.tuda.prg.parser.visitorsremotecom.EnclaveClassDeclarationVisitorComm;

import java.util.HashSet;
import java.util.Set;

public class UserClassTranslationUtils {

    public static void translateUserClass(final CompilationUnit cu) {
        UserDefinedClassVisitor userClassVisitorJe = new UserDefinedClassVisitor();
        userClassVisitorJe.visit(cu, null);
    }

    public static void addSerializableToUserClass(final CompilationUnit cu) {
        ImplementSerializableInterface implementSerializableInterfaceVisitor = new ImplementSerializableInterface();
        implementSerializableInterfaceVisitor.visit(cu, null);
    }
}