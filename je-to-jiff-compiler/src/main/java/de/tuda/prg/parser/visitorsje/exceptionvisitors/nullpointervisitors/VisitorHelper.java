package de.tuda.prg.parser.visitorsje.exceptionvisitors.nullpointervisitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;

import java.util.List;
import java.util.Random;

public class VisitorHelper {

    public static TryStmt wrapBodyWithoutDecl(List<Expression> nullPointers) {
        TryStmt ts = new TryStmt(); // initialize try stmt
        BlockStmt bs = new BlockStmt();
        boolean isArrayAccess = false;
        for (int i=0; i<nullPointers.size(); i++) {
            if (nullPointers.get(i).isAssignExpr()) {
                if (((AssignExpr) nullPointers.get(i)).getTarget().isArrayAccessExpr() ||
                        ((AssignExpr) nullPointers.get(i)).getTarget().isArrayAccessExpr()) {
                    isArrayAccess = true;
                }
            }
            if (nullPointers.get(i).isArrayAccessExpr()) isArrayAccess = true;
            bs.addStatement(nullPointers.get(i));
        }
        ts.setTryBlock(bs); // Set Body of try block
        CatchClause cc = new CatchClause(); // initialize cc
        String exceptionName = "e"; // Exception name is e
        cc.setParameter(new Parameter().setName(exceptionName).setType(NullPointerException.class)); // Set Parameter of Exception to handle to NullPointerException
        NodeList<CatchClause> catchClauses = new NodeList<>(cc);
        if (isArrayAccess) {
            CatchClause cc2 = new CatchClause(); // initialize cc
            cc2.setParameter(new Parameter().setName(exceptionName).setType(ArrayIndexOutOfBoundsException.class)); // Set Parameter of Exception to handle to NullPointerException
            catchClauses.add(cc2);
            /*CatchClause cc3 = new CatchClause(); // initialize cc
            cc3.setParameter(new Parameter().setName(exceptionName).setType(NegativeArraySizeException.class)); // Set Parameter of Exception to handle to NullPointerException
            catchClauses.add(cc3);*/
        }

        BlockStmt cb = new BlockStmt(); // initialize Body for cc
        cc.setBody(cb); // Set body to empty body of cc
        ts.setCatchClauses(catchClauses); // Set CCs to List with one cc
        return ts; // Return try stmt
    }

    public static TryStmt wrapBody(List<Expression> nullPointers, List<NameExpr> var) {
        TryStmt ts = new TryStmt(); // initialize try stmt
        BlockStmt bs = new BlockStmt();
        boolean isArrayAccess = false;
        for (int i=0; i<nullPointers.size(); i++) {
            if (nullPointers.get(i).isAssignExpr()) {
                if (((AssignExpr) nullPointers.get(i)).getTarget().isArrayAccessExpr() ||
                        ((AssignExpr) nullPointers.get(i)).getTarget().isArrayAccessExpr()) {
                    isArrayAccess = true;
                }
            }
            if (nullPointers.get(i).isArrayAccessExpr()) isArrayAccess = true;
            bs.addStatement(makeAssignment(nullPointers.get(i), var.get(i)));
        }
        ts.setTryBlock(bs);



        CatchClause cc = new CatchClause(); // initialize cc
        String exceptionName = "e"; // Exception name is e
        cc.setParameter(new Parameter().setName(exceptionName).setType(NullPointerException.class)); // Set Parameter of Exception to handle to NullPointerException
        NodeList<CatchClause> catchClauses = new NodeList<>(cc);
        if (isArrayAccess) {
            CatchClause cc2 = new CatchClause(); // initialize cc
            cc2.setParameter(new Parameter().setName(exceptionName).setType(ArrayIndexOutOfBoundsException.class)); // Set Parameter of Exception to handle to NullPointerException
            catchClauses.add(cc2);
        }


        BlockStmt cb = new BlockStmt(); // initialize Body for cc
        cc.setBody(cb); // Set body to empty body of cc
        ts.setCatchClauses(catchClauses); // Set CCs to List with one cc
        return ts; // Return try stmt
    }

    public static AssignExpr makeAssignment(Node initializier, VariableDeclarator vd) {
        AssignExpr assignExpr = new AssignExpr();
        assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
        assignExpr.setTarget(vd.getNameAsExpression().clone());
        assignExpr.setValue((Expression) initializier.clone());
        return assignExpr;
    }

    public static AssignExpr makeAssignment(Expression value, NameExpr target) {
        AssignExpr assignExpr = new AssignExpr();
        assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
        assignExpr.setTarget(target.clone());
        assignExpr.setValue(value.clone());
        return assignExpr;
    }

    public static NameExpr makeNameExpr(List<NameExpr> names) {
        NameExpr var;
        do {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = names.size() / 26 + 1;
            Random random = new Random();

            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            var = new NameExpr(generatedString);
        } while (names.contains(var));

        return var;
    }

    public static String getInitializerByType(Type t) {
        switch (t.asString()) {
            case "int":
                return "0";
            case "float":
                return "0.0";
            case "Object":
                return "null";
            case "String":
                return "new String()";
            case "boolean":
                return "false";
            case "char":
                return "\' \'";
            case "double":
                return "0.0";
            default:
                return "null";
        }
    }

    public static NameExpr getNameExpr(Expression n) {
        if (n.isNameExpr()) {
            return (NameExpr) n;
        } else if (n.isArrayAccessExpr()) {
            Expression iter = (ArrayAccessExpr) n;
            while(iter.isArrayAccessExpr()) {
                iter = ((ArrayAccessExpr)iter).getName();
            }
            if (iter.isNameExpr()) return (NameExpr) iter;
        }
        return null;
    }

    public static Type getTypeExpr(Expression n) {
        System.out.println("-----------GETTYPEEXPRESSION: "+n.toString()+"------------");
        String exp = n.toString();
        if (exp.contains("equals")) {
            return PrimitiveType.booleanType();
        } else if (exp.contains("size")) {
            return PrimitiveType.intType();
        } if (exp.contains("length")) {
            return PrimitiveType.intType();
        } else {
            return null;
        }
    }

}
