package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;

public class StatementExpressionVisitor { // Combines Declarations and Assignments for ExpressionsStatements
    DeclarationVisitor dVisitor = new DeclarationVisitor();
    AssignmentVisitor aVisitor = new AssignmentVisitor();

    public void startVisiting(CompilationUnit n, Object arg) {
        dVisitor.startVisiting(n, arg);
        aVisitor.startVisiting(n, arg);
    }

    public void startVisiting(BlockStmt n, Object arg) {
        dVisitor.startVisiting(n, arg);
        aVisitor.startVisiting(n, arg);
    }


}
