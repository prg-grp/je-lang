package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementVisitor extends VoidVisitorAdapter<Object> { // MyVisitor is VoidVisitor for Task 1, as nothing is modified in the AST directly

    private StatementExpressionVisitor visitor = new StatementExpressionVisitor();

    private List<NameExpr> usedNames;
    private List<NameExpr> names;
    private List<Expression> toReplace;
    private HashMap<NameExpr, Type> types;
    private Type rType;

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
        if (arg instanceof Type) rType = (Type) arg;
        AttributeVisitor attributeVisitor = new AttributeVisitor();
        types = attributeVisitor.startVisiting((CompilationUnit) n.findRootNode(), arg);
        NameExpressionVisitor nameExpressionVisitor = new NameExpressionVisitor();
        usedNames = nameExpressionVisitor.startVisiting(n, arg);
        visit(n, arg);
    }

    /**
     * Visit Binary as super
     * @param n
     * @param arg
     */
    @Override
    public void visit(BinaryExpr n, Object arg) {
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
     * Visit VariableDeclarator and put the names in the HashMap
     * @param n
     * @param arg
     */
    @Override
    public void visit(VariableDeclarator n, Object arg) {
        types.put(n.getNameAsExpression(), n.getType().getElementType());
    }


    /**
     * visit IfStmt, accept first the condition.
     * Afterwards check StatementExpressions and Statements in ThenStmt and possible ElseStmt
     * @param n
     * @param arg
     */
    @Override
    public void visit(IfStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        visitor.startVisiting((BlockStmt) n.getThenStmt(), arg);
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt().isPresent()) {
            visitor.startVisiting((BlockStmt) n.getElseStmt().get(), arg);
            n.getElseStmt().get().accept(this, arg);
        }
    }

    /**
     * visit DoStmt, accept first the condition.
     * Afterwards check StatementExpressions and Statements in the Body
     * @param n
     * @param arg
     */
    @Override
    public void visit(DoStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        visitor.startVisiting((BlockStmt) n.getBody(), arg);
        n.getBody().accept(this, arg);
    }

    /**
     * visit ForEachStmt, accept first the iterable.
     * Afterwards check StatementExpressions and Statements in the Body and possible ElseStmt
     * @param n
     * @param arg
     */
    @Override
    public void visit(ForEachStmt n, Object arg) {
        n.getIterable().accept(this, arg);
        visitor.startVisiting((BlockStmt) n.getBody(), arg);
        n.getBody().accept(this, arg);
    }

    /**
     * visit ForStmt, accept first the initialization, compare and update.
     * Afterwards check StatementExpressions and Statements in Body and possible ElseStmt
     * @param n
     * @param arg
     */
    @Override
    public void visit(ForStmt n, Object arg) {
        n.getInitialization().accept(this, arg);
        if (n.getCompare().isPresent()) n.getCompare().get().accept(this, arg);
        n.getUpdate().accept(this, arg);
        visitor.startVisiting((BlockStmt) n.getBody(), arg);
        n.getBody().accept(this, arg);
    }

    /**
     * visit ReturnStmt, accept first the condition.
     * Afterwards check StatementExpressions and Statements in ThenStmt and possible ElseStmt
     * @param n
     * @param arg
     */
    @Override
    public void visit(ReturnStmt n, Object arg) {
        if (n.getExpression().isPresent()) {
            n.getExpression().get().accept(this, arg);
        }
    }

    /**
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(WhileStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        visitor.startVisiting((BlockStmt) n.getBody(), arg);
        n.getBody().accept(this, arg);
    }

    //TODO implement SwitchStmt
    @Override
    public void visit(SwitchStmt n, Object arg) {
        super.visit(n, arg);
    }

    
    /**
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(BlockStmt n, Object arg) {
        System.out.println("-----------STARTED VISITING BLOCKSTATEMENT------------");
        List<NameExpr> names_old = names;
        List<Expression> toReplace_old = toReplace;
        n.getStatements()
                .forEach(p -> {
                    names = new ArrayList<>();
                    toReplace = new ArrayList<>();
                    if (p!=null) {
                        if (!p.isExpressionStmt()) p.accept(this, arg);
                        if (toReplace.size()>0) {
                            BlockStmt bs = new BlockStmt();
                            System.out.println("-----------TO REPLACE : "+toReplace.get(0).toString()+"------------");
                            Expression expression = toReplace.get(0);
                            Type t;
                            if (expression.isMethodCallExpr() || expression.isFieldAccessExpr()) {
                                System.out.println("-----------EXTRACT RETURN TYPE OF MC OR ACC------------");
                                t = VisitorHelper.getTypeExpr(expression);
                            } else {
                                NameExpr var = VisitorHelper.getNameExpr(expression);
                                t = types.get(var);
                            }
                            if (t==null) {
                                System.out.println("RTYPE = ");
                                t = rType;
                            }

                            VariableDeclarationExpr vdExp = new VariableDeclarationExpr();
                            NodeList nl = new NodeList();
                            for (int i = 0; i < names.size(); i++) {
                                VariableDeclarator vd = new VariableDeclarator();
                                vd.setType(t);
                                vd.setName(names.get(i).getName());
                                vd.setInitializer(VisitorHelper.getInitializerByType(t));
                                nl.add(vd);
                            }
                            vdExp.setVariables(nl);
                            bs.addStatement(vdExp);
                            bs.addStatement(VisitorHelper.wrapBody(toReplace, names));
                            bs.addStatement(p.clone());
                            p.replace(bs);
                        }
                    }
                });
        System.out.println("-----------ENDED VISITING BLOCKSTATEMENT------------");
        names = names_old;
        toReplace = toReplace_old;
    }
}