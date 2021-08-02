package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.util.*;

public class AssignmentVisitor extends VoidVisitorAdapter<Object> { // MyVisitor is VoidVisitor for Task 1, as nothing is modified in the AST directly

    /**
     * All used Variablenames in Java File
     */
    private List<NameExpr> usedNames;
    /**
     * All replacement variable names
     */
    private List<NameExpr> names;
    /**
     * All Expressions that should be wrapped
     */
    private List<Expression> toReplace;
    /**
     * All Types of all Variables
     */
    private HashMap<NameExpr, Type> types;
    /**
     * Variable Name of assignment
     */
    private NameExpr var;

    /**
     * Start Visiting calls visit on the received root node and puts and declares the Lists for the Nodes
     * @param n CompilationUnit (Root Node of AST)
     * @param arg
     * @return visitMap
     */
    public void startVisiting(CompilationUnit n, Object arg) {
        types = new HashMap<>();
        NameExpressionVisitor nameExpressionVisitor = new NameExpressionVisitor();
        usedNames = nameExpressionVisitor.startVisiting(n, arg);
        visit(n, arg);
    }

    /**
     * Start Visiting calls visit on the received node and puts and declares the Lists for the Nodes.
     * Initialize HashMap with AttributeVisitor
     * @param n BlockStmt
     * @param arg
     */
    public void startVisiting(BlockStmt n, Object arg) {
        AttributeVisitor attributeVisitor = new AttributeVisitor();
        types = attributeVisitor.startVisiting((CompilationUnit) n.findRootNode(), arg);
        NameExpressionVisitor nameExpressionVisitor = new NameExpressionVisitor();
        usedNames = nameExpressionVisitor.startVisiting(n, arg);
        visit(n, arg);
    }

    /**
     * Visit ExpressionStmt as super
     * @param n
     * @param arg
     */
    @Override
    public void visit(ExpressionStmt n, Object arg) {
        super.visit(n, arg);
    }

    /**
     * Visit VariableDeclarator and put the names in the HashMap
     * @param n
     * @param arg
     */
    @Override
    public void visit(VariableDeclarator n, Object arg) {
        types.put(n.getNameAsExpression(), n.getType().getElementType());
    }

    /**
     * Visit Assignexpression.
     * If the target is a name Expression, set var to that Name
     * If it is a possible Nullpointer, wrap that whole thing up as something should be assigned to it
     * @param n
     * @param arg
     */
    @Override
    public void visit(AssignExpr n, Object arg) {
        if (n.getTarget().isNameExpr()) {
            var = (NameExpr) n.getTarget();
        } else if (n.getTarget().isFieldAccessExpr() || n.getTarget().isMethodCallExpr() || n.getTarget().isArrayAccessExpr() ) {
            toReplace.add(n.clone());
            return;
        }
        super.visit(n, arg);
    }

    /**
     * Visits MethodCallExpr. If a scope is present, replace with a new Variable.
     * @param n
     * @param arg
     */
    @Override
    public void visit(MethodCallExpr n, Object arg) {
        if (n.getScope().isPresent()) {
            toReplace.add(n.clone());
            NameExpr name = VisitorHelper.makeNameExpr(usedNames);
            usedNames.add(name);
            names.add(name);
            n.replace(name);
        } else super.visit(n, arg);
    }

    /**
     * Visits FieldAccessExpr. Replace with a new Variable
     * @param n
     * @param arg
     */
    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        toReplace.add(n.clone());
        NameExpr name = VisitorHelper.makeNameExpr(usedNames);
        usedNames.add(name);
        names.add(name);
        n.replace(name);
    }

    /**
     * Visits ArrayAccessExpr. Replace with a new Variable.
     * @param n
     * @param arg
     */
    @Override
    public void visit(ArrayAccessExpr n, Object arg) {
        toReplace.add(n.clone());
        NameExpr name = VisitorHelper.makeNameExpr(usedNames);
        usedNames.add(name);
        names.add(name);
        n.replace(name);
    }

    /**
     * Visit BinaryExpr as super.visit()
     * @param n
     * @param arg
     */
    @Override
    public void visit(BinaryExpr n, Object arg) {
        super.visit(n, arg);
    }

    /**
     * Visits BlockStmt and replace/manipulate with TryStatements
     * @param n
     * @param arg
     */
    @Override
    public void visit(BlockStmt n, Object arg) {
        n.getStatements().forEach(statement -> {
            names = new ArrayList<>();
            toReplace = new ArrayList<>();
            var = null;
            if (statement instanceof ExpressionStmt) {
                statement.accept(this, arg); // Visit ExpressionStmt
                if (toReplace.size()>0) { // Something has to be replaced
                    BlockStmt bs = new BlockStmt(); // Make a new BlockStmt
                    if (var != null) { // Check if something was assigned
                        Type t = types.get(var); // Take the type out of the Map
                        if (t==null) {
                            t = new ClassOrInterfaceType("Object"); // Set to Object if not known
                        }

                        VariableDeclarationExpr vdExp = new VariableDeclarationExpr(); // Make a new VariableDeclarationExpr
                        NodeList nl = new NodeList();
                        for (int i = 0; i < names.size(); i++) { // Initialize all Variables with default values and put them into a Nodelist
                            VariableDeclarator vd = new VariableDeclarator();
                            vd.setType(t);
                            vd.setName(names.get(i).getName());
                            vd.setInitializer(VisitorHelper.getInitializerByType(t));
                            nl.add(vd);
                        }
                        vdExp.setVariables(nl); // Set Variables to Nodelist
                        bs.addStatement(vdExp); // Add vdExp to bs
                        bs.addStatement(VisitorHelper.wrapBody(toReplace, names)); // Wrap the body and assign the variables to the nullPointers
                        bs.addStatement(statement.clone()); // Add the assignexpression with the replaced Nullpointers
                        statement.replace(bs); // replace old ExpressionStmt with the new BlockStmt
                    } else { // If nothing was assigned, wrap the possible Nullpointer without Declaration
                        TryStmt ts = VisitorHelper.wrapBodyWithoutDecl(toReplace);
                        statement.replace(ts);
                    }
                }
            }
        });
    }
}
