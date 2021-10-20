package de.tuda.prg.taskprocessing;

import de.tuda.prg.entities.ClassNameMethodDecls;

import java.util.ArrayList;
import java.util.Map;

import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GlobalTaskData {

    final HashSet<String> enclaveClassNames = new HashSet<>();
    final HashSet<String> gatewayMethodNames = new HashSet<>();
    final List<ClassNameMethodDecls> gatewayMethodDeclarations = new ArrayList<>();
    final Map<String, List<String>> gatewayMethodCalls = new HashMap<>();
    final HashSet<String> enclaveClassesToExposeNames = new HashSet<String>();  // Names of the enclave wrapper classes to be bound to the RMI registry.

}


