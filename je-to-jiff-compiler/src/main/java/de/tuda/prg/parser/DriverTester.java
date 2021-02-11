package de.tuda.prg.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;

public class DriverTester {

    private static final String FILE_PATH = "je-to-jiff-compiler/Point.java";

    public static void main(String[] args) {
        try {
            final CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
