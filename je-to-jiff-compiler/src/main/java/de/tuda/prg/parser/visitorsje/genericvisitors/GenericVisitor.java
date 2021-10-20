package de.tuda.prg.parser.visitorsje.genericvisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class GenericVisitor extends VoidVisitorAdapter {
    List<String> generics;
    /**
     * Start Visiting calls visit on the received root node and puts and declares the Lists for the Nodes
     * @param n CompilationUnit (Root Node of AST)
     * @param arg
     * @return visitMap
     */
    public List<String> startVisiting(CompilationUnit n, Object arg) {
        generics = new ArrayList<>();
        n.accept(this, arg);
        return generics;
    }

    @Override
    public void visit(ClassOrInterfaceType n, Object arg) {
        if (n.getChildNodes().size()==2) {
            if ((n.getChildNodes().get(1)) instanceof ClassOrInterfaceType) {
                generics.add(((ClassOrInterfaceType) n.getChildNodes().get(1)).asString());
                n.getChildNodes().get(1).removeForced();
            }
        }
    }
}
