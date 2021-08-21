package de.tuda.prg.parser.visitorsje.genericvisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableVisitor extends VoidVisitorAdapter {
    Map<String, String> variables;
    /**
     * Start Visiting calls visit on the received root node and puts and declares the Lists for the Nodes
     * @param n CompilationUnit (Root Node of AST)
     * @param arg
     * @return visitMap
     */
    public Map<String, String> startVisiting(CompilationUnit n, List<String> arg) {
        variables = new HashMap<>();
        n.accept(this, arg);
        return variables;
    }

    @Override
    public void visit(VariableDeclarator n, Object arg) {
        if (((List<String>) arg).contains(n.getType().asString())) {
            variables.put(n.getNameAsString(), n.getType().asString());
        }
    }
}
