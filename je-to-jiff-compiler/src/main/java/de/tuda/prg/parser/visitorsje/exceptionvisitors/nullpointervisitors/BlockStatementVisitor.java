package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class BlockStatementVisitor extends VoidVisitorAdapter {
    StatementExpressionVisitor statementExpressionVisitor = new StatementExpressionVisitor(); // Visits all ExpressionStatements
    StatementVisitor statementVisitor = new StatementVisitor(); // Visit all other Statements

    //Input is Compilation Unit
    public void startVisiting(CompilationUnit n, Object arg) {
        visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Object arg) { // Visit all implemented Methods
        if (n.getBody().isPresent()) {
            statementExpressionVisitor.startVisiting(n.getBody().get(), arg);
            statementVisitor.startVisiting(n.getBody().get(), arg);
        }
    }

    @Override
    public void visit(ConstructorDeclaration n, Object arg) { // Visit all implemented Constructors
        statementExpressionVisitor.startVisiting(n.getBody(), arg);
        statementVisitor.startVisiting(n.getBody(), arg);
    }
}
