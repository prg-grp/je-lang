package de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.resolver.TypeResolver;

public class Method{

    private NodeList<Modifier> modifiers;
    private String name;
    private String returnType;
    private List<String> argumentTypes;

    /**
     * Build a Method object for a given MethodDeclaration and some metadata given by the CompilationUnitMetaDataContainer.
     * @param methodDeclaration MethodDeclaration
     * @param metaDataContainer Metadata
     * @return Method
     */
    public static Method fromDeclaration(MethodDeclaration methodDeclaration, CompilationUnitMetaDataContainer metaDataContainer) {
        Method method = new Method();
        method.setModifiers(methodDeclaration.getModifiers());
        method.setName(methodDeclaration.getNameAsString());
        String returnType = TypeResolver.resolve(methodDeclaration.getType(), metaDataContainer);
        List<String> argumentTypes = new ArrayList<>();
        for(Parameter parameter: methodDeclaration.getParameters()) {
            argumentTypes.add(TypeResolver.resolve(parameter.getType(), metaDataContainer));
        }
        method.setReturnType(returnType);
        method.setArgumentTypes(argumentTypes);
        return method;
    }

    public Method(){}

    @Override
    public String toString() {
        String template = "";
        template += "name: " + this.name + "\n";
        template += "modifiers: " + this.modifiers.toString() + "\n";
        template += "returnType: " + this.returnType + "\n";
        template += "argumentTypes: " + this.argumentTypes.toString();
        return template;
    }


    public NodeList<Modifier> getModifiers() {
        return this.modifiers;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getArgumentTypes() {
        return argumentTypes;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setArgumentTypes(List<String> argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public void setModifiers(NodeList<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    /**
     * Check whether two Method objects contain the same information.
     * Returns true if:
     *  1. Names are equal
     *  2. Return type is equal
     *  3. Argument types are all equal
     *  4. Modifiers are equal
     * @param challenge To be compared
     * @return Boolean
     */
    public boolean equals(Method challenge) {
        List<String> challengeModifiers = challenge.getModifiers().stream().map(Node::toString)
                .collect(Collectors.toList());
        List<String> thisModifiers = this.modifiers.stream().map(Node::toString)
                .collect(Collectors.toList());
        return challengeModifiers.equals(thisModifiers) &&
                this.returnType.equals(challenge.getReturnType()) &&
                this.argumentTypes.equals(challenge.getArgumentTypes()) &&
                this.name.equals(challenge.getName());
    }
}
