package de.tuda.prg.entities;

import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.ArrayList;
import java.util.List;

public class ClassNameMethodDecls {

    private String enclaveClassName;
    private List<MethodDeclaration> methodDeclarations = new ArrayList<>();

    public ClassNameMethodDecls(String enclaveClassName) {
        this.enclaveClassName = enclaveClassName;
    }

    public String getEnclaveClassName() {
        return enclaveClassName;
    }

    public void setEnclaveClassName(String enclaveClassName) {
        this.enclaveClassName = enclaveClassName;
    }

    public List<MethodDeclaration> getMethodDeclarations() {
        return methodDeclarations;
    }
}
