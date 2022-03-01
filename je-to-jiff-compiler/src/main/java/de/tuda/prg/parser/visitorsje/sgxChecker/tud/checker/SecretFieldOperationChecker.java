package de.tuda.prg.parser.visitorsje.sgxChecker.tud.checker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.regex.FieldDetector;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.Field;
import de.tuda.prg.parser.visitorsje.sgxChecker.tud.collectors.Collector;

/**
 * This class checks a given method for reassignments of @Secret variables
 */
public class SecretFieldOperationChecker{

    private static final Logger logger = LogManager.getLogger(SecretFieldOperationChecker.class);

    private List<Field> secretFields;
    private List<String> localVars;
    private final FieldDetector fieldDetector = new FieldDetector();

    /**
     * Entry point to check occurrences of reassignments of @Secret-variables.
     * This method first extracts all local variables and assignment-expressions from the
     * method. After this it checks for each assignment whether a reassignment was made using
     * the checkExpression-method described below.
     * @param method MethodDeclaration
     * @param secretFields Array of @Secret-fields
     */
    public void check(MethodDeclaration method, List<Field> secretFields) {
        this.secretFields = secretFields;
        this.localVars = this.extractLocalMethodVariables(method);
        List<AssignExpr> assignExprs = method.findAll(AssignExpr.class);
        List<VariableDeclarator> declarations = method.findAll(VariableDeclarator.class);
        List<MethodCallExpr> methodCallExprs = method.findAll(MethodCallExpr.class);
        List<AssignExpr> failedExpressions = assignExprs.stream().filter(this::checkExpression).collect(Collectors.toList());
        List<VariableDeclarator> failedDeclarations = declarations.stream().filter(this::checkExpression).collect(Collectors.toList());
        List<MethodCallExpr> failedSecretMethodCalls = methodCallExprs.stream().filter(this::checkMethodCallOnSecret).collect(Collectors.toList());
        if(!failedDeclarations.isEmpty()) {
            for(VariableDeclarator declarator: failedDeclarations) {
                this.log(declarator);
            }
        }
        if(!failedExpressions.isEmpty()) {
            for(AssignExpr expr: failedExpressions) {
                this.log(expr);
            }
        }
        if(!failedSecretMethodCalls.isEmpty()) {
            for(MethodCallExpr expr: failedSecretMethodCalls) {
                this.log(expr);
            }
        }
    }

    private void log(VariableDeclarator expression) {
        if(expression.getRange().isPresent()) {
            logger.warn("[Line: "+expression.getRange().get().begin.line+"] Detected @Secret field in declaration: "+expression.toString());
        }
        else {
            logger.warn("[Missing line] Detected @Secret field in declaration: %s".format(expression.toString()));
        }
    }

    private void log(AssignExpr expression) {
        if(expression.getRange().isPresent()) {
            logger.error("[Line: "+expression.getRange().get().begin.line+"] Detected reassignment of @Secret field: "+expression.toString());
        }
        else {
            logger.error("[Missing line] Detected reassignment of @Secret field: %s".format(expression.toString()));
        }
    }

    private void log(MethodCallExpr expression) {
        if(expression.getRange().isPresent()) {
            logger.warn("[Line: "+expression.getRange().get().begin.line+"] Detected method calls on @Secret field: "+expression.toString()+". The object may change its state.");
        }
        else {
            logger.warn("[Missing line] Detected method calls on @Secret field: %s. The object may change its state.".format(expression.toString()));
        }
    }

    /**
     * This method checks for a given expression if a @Secret variable was either used on the
     * left or on the right side of the assignment-expression. It returns true if this is the case
     * There are 2 cases to check:
     * 1. The @Secret variable is static or referenced using "this" -> the @Secret variable must be
     *  the entry of position 1 if the expression is split on the dot.
     * 2. The @Secret variable is referenced using implicit "this" -> the @Secret variable must not be
     *  in the array of local method variables and it has to be in the array of @Secret variables.
     * @param expression Expression to be checked (always an assignment)
     * @return True if any of the conditions hold
     */
    private boolean checkExpression(AssignExpr expression) {
        Expression left = expression.getTarget();
        Expression right = expression.getValue();
        return this.checkLeftExpression(left) || this.checkRightExpression(right);
    }

    /**
     * This method checks declaration of variables. It checks only the right hand side of a declaration
     * since it is not necessary to know something about the left hand side of the declaration.
     * If on the right side a secret variable was used in a statement (except method calls which will be handled
     * separately) it will be illegal.
     * @param declarator Declaration to be checked
     * @return Boolean
     */
    private boolean checkExpression(VariableDeclarator declarator) {
        Optional<Expression> optionalExpression = declarator.getInitializer();
        if(optionalExpression.isPresent()) {
            Expression right = optionalExpression.get();
            return this.checkRightExpression(right);
        }
        return false;
    }

    /**
     * Extract all local variable names as a string.
     * @param method Method of which the variables should be extracted.
     * @return Array of all local variable names.
     */
    private List<String> extractLocalMethodVariables(MethodDeclaration method) {
        return method.findAll(VariableDeclarator.class)
                .stream()
                .map(localVar -> localVar.getName().toString())
                .collect(Collectors.toList());
    }

    /**
     * This method checks the right hand side of an expression.
     * It is called for both AssignExpr and VariableDeclarator-types.
     * It first extracts all tokens matching a Java-conform field and checks then
     * if the field name is contained in the secret field-array.
     * If the case it returns true, else false
     * @param expression Expression to be checked
     * @return Boolean
     */
    private boolean checkRightExpression(Expression expression) {
        List<String> foundFields = this.fieldDetector.getMatches(expression.toString());
        return foundFields.stream().anyMatch(this::checkSecretFieldUsage);
    }

    /**
     * This method is only called for AssignExpr-types, i.e. only if we obtain a reassignment of a variable.
     * The left hand side of an assignment can only have the form
     *  1. variable = someValue;
     *  2. this.variable = someValue;
     *  3. CLASSNAME.staticVariable = someValue;
     * All three cases will be handled here.
     * The method returns true if a secret variable was reassigned.
     * @param expression Expression to be checked
     * @return Boolean
     */
    private boolean checkLeftExpression(Expression expression) {
        return this.checkSecretFieldUsage(expression.toString());
    }

    /**
     * This method checks for usage of secret variables.
     * It covers the following cases:
     *  1. use like "this.secret"
     *  2. use like "secret"
     *  3. use like "CLASS.staticSecret"
     * It returns true if a secret variable is used in the given expression-string
     * @param expression Expression as string
     * @return Boolean
     */
    private boolean checkSecretFieldUsage(String expression) {
        String[] splitOnDot = expression.split("\\.");
        if(splitOnDot.length == 1) {
            String fieldName = splitOnDot[0];
            if(!this.localVars.contains(fieldName)) {
                return this.secretFields.toString().contains(fieldName);
            }
        }
        else if(splitOnDot.length > 1) {
            String fieldName = splitOnDot[1];
            if(!this.localVars.contains(fieldName)) {
                return this.secretFields.toString().contains(fieldName);
            }
        }
        return false;
    }

    /**
     * Check if a method call is made on a secret variable.
     * @param methodCallExpr Method call expression to be analyzed
     * @return Boolean
     */
    private boolean checkMethodCallOnSecret(MethodCallExpr methodCallExpr) {
        String call = methodCallExpr.toString();
        List<String> splitCall = Arrays.asList(call.split("\\."));
        String potentialSecret = splitCall.get(0);
        List<String> secretNames = this.secretFields.stream().map(Field::getName).collect(Collectors.toList());
        return secretNames.contains(potentialSecret);
    }
}
