package de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures;

import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton which holds the SymbolSolver and a list with all Gateway-Methods from all class files detected by the SGXChecker.
 */
public final class SymbolSolverSingleton {

    private static SymbolSolverSingleton symbolSolverSingleton;
    //The SymbolSolver is set by the SGXChecker at the beginning (SGXChecker.configureStaticJavaParser()).
    private JavaParserFacade symbolSolver;
    //The Gateway register is filled in the Collector class
    public List<String> gatewayRegister;

    private SymbolSolverSingleton(){
        gatewayRegister = new ArrayList<>();
    }

    public static SymbolSolverSingleton getInstance() {
        if(symbolSolverSingleton == null){
            symbolSolverSingleton = new SymbolSolverSingleton();
        }
        return symbolSolverSingleton;
    }

    public void setSymbolSolver(TypeSolver typeSolver){
        symbolSolver = JavaParserFacade.get(typeSolver);
    }

    public JavaParserFacade getSymbolSolver(){
        return symbolSolver;
    }

    public String toString(){
        return gatewayRegister + " " + symbolSolver.toString();
    }

}
