package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class DeclarationVisitor extends VoidVisitorAdapter {

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
     * current type of the declaration
     */
    private Type type;
    /**
     * Variable Name of declaration
     */
    private NameExpr var;
    /**
     * AssignExpr for the variable
     */
    private AssignExpr assignExpr;

    /**
     * Start Visiting calls visit on the received root node and puts and declares the Lists for the Nodes
     * @param n CompilationUnit (Root Node of AST)
     * @param arg
     */
    public void startVisiting(CompilationUnit n, Object arg) {
        NameExpressionVisitor nameExpressionVisitor = new NameExpressionVisitor();
        usedNames = nameExpressionVisitor.startVisiting(n, arg);
        visit(n, arg);
    }

    /**
     * Start Visiting calls visit on the received node and puts and declares the Lists for the Nodes
     * @param n BlockStmt
     * @param arg
     */
    public void startVisiting(BlockStmt n, Object arg) {
        NameExpressionVisitor nameExpressionVisitor = new NameExpressionVisitor();
        usedNames = nameExpressionVisitor.startVisiting(n, arg);
        visit(n, arg);
    }


    /**
     * Visits ExpressionStatement only if it is a VariableDecl
     * @param n ExpressionStmt
     * @param arg
     */
    @Override
    public void visit(ExpressionStmt n, Object arg) {
        if(n.getExpression().isVariableDeclarationExpr()) super.visit(n, arg);
    }

    /**
     * Visits VariableDeclarator if Initializer is present. Sets the type of the Declaration.
     * Sets the variable name and visits its children. If something within the declaration
     * has to be replaced, make an AssignExpression for TryStatement and remove the Initializer
     * as the assignment has to be in the TryStatement
     * @param n VariableDeclarator
     * @param arg
     */
    @Override
    public void visit(VariableDeclarator n, Object arg) {
        if (n.getInitializer().isPresent()) {
            type = n.getType();
            var = n.getNameAsExpression();
            super.visit(n, arg);
            if (toReplace.size() > 0) {
                assignExpr = VisitorHelper.makeAssignment(n.getInitializer().get(), n);
                n.removeInitializer();
            }
        }
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
        for(int j = 0; j<n.getStatements().size(); j++) {
            Statement statement = n.getStatement(j);
            names = new ArrayList<>();
            toReplace = new ArrayList<>();
            var = null;
            if (statement instanceof ExpressionStmt) {
                statement.accept(this, arg); // Visit ExpressionStmt
                if (toReplace.size()>0) { // Something has to be replaced
                    BlockStmt bs = new BlockStmt(); // Make a new BlockStmt
                    if (var != null) {
                        VariableDeclarationExpr vdExp = new VariableDeclarationExpr(); // Make a new VariableDeclarationExpr
                        NodeList nl = new NodeList();
                        for (int i = 0; i < names.size(); i++) { // Initialize all Variables with default values and put them into a Nodelist
                            VariableDeclarator vd = new VariableDeclarator();
                            vd.setType(type);
                            vd.setName(names.get(i).getName());
                            vd.setInitializer(VisitorHelper.getInitializerByType(type));
                            nl.add(vd);
                        }
                        vdExp.setVariables(nl); // Set Variables to Nodelist
                        bs.addStatement(vdExp); // Add vdExp to bs
                        bs.addStatement(VisitorHelper.wrapBody(toReplace, names)); // Wrap the body and assign the variables to the nullPointers
                        bs.addStatement(assignExpr); // Add the assignexpression with the replaced Nullpointers
                        n.getStatements().add(n.getStatements().indexOf(statement)+1, bs); // add the whole bs after the variable declaration
                        j++;
                    }
                }
            }
        }
    }
}
