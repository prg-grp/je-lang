package de.tuda.prg.parser.visitorsje.sanitizecheckvisitors;

import java.util.Optional;
import java.util.function.Predicate;
import java.lang.StringIndexOutOfBoundsException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.tuda.prg.constants.Codes;
import de.tuda.prg.parser.ParserHelper;
import de.tuda.prg.parser.visitorsje.genericvisitors.GenericVisitor;


public class SanitizeVisitor extends VoidVisitorAdapter<Boolean> {
    private String endorseVariable = "";

    @Override
    public void visit(MethodDeclaration mc, Boolean arg) {
        System.out.println("Visiting following mc : "+mc.toString());
        boolean isGateway = false;
        for(AnnotationExpr aExpr : mc.getAnnotations()) {
            if (aExpr.toString().contains("Gateway")) isGateway = true;
        }
        System.out.println("Is GW Method : "+isGateway);
        if (isGateway) super.visit(mc, true);
        else return;
    }

    @Override
    public void visit(BlockStmt bs, Boolean arg) {
        if (arg == true) {

            BlockStmt newStmt = new BlockStmt();
            boolean sanitized = false;
            boolean endorsed = false;
            //System.out.println("Check BlockStatement : "+bs.toString());
            for (int i = 0; i<bs.getStatements().size(); i++) {
                Statement stmt = bs.getStatements().get(i);
                //System.out.println("Current Stmt : "+stmt);
                if(stmt.isIfStmt()) {
                    sanitized = checkIf((IfStmt) stmt);
                    if (!sanitized) newStmt.addStatement(stmt.clone());
                } else if (!sanitized || !endorsed) {
                    endorsed = checkEndorse(stmt);
                    newStmt.addStatement(stmt.clone());
                } else if (endorsed && sanitized) {
                    IfStmt sanitize = new IfStmt();
                    sanitize.setCondition(StaticJavaParser.parseExpression(Codes.sanitize+"("+endorseVariable+")"));
                    BlockStmt body = new BlockStmt();
                    for (int j = i; j<bs.getStatements().size(); j++) {
                        body.addStatement(bs.getStatements().get(j).clone());
                    }
                    sanitize.setThenStmt(body);
                    sanitize.setElseStmt(StaticJavaParser.parseStatement("return null;"));
                    newStmt.addStatement(sanitize);
                    bs.replace(newStmt);
                    return;
                }
            }
        }
    }

    private boolean checkIf(IfStmt ifStmt) {
        System.out.println("IF TO STRING: "+ifStmt.toString());
        if (ifStmt.getCondition().toString().contains(Codes.sanitize)){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkEndorse(Statement stmt) {
        if (stmt.toString().contains(Codes.endorse)){
            endorseVariable = stmt.toString().split(" ")[1];
            return true;
        } else {
            return false;
        }
    }
    
}