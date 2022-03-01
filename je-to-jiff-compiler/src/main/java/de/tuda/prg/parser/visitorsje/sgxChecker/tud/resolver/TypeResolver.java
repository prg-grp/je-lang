package de.tuda.prg.parser.visitorsje.sgxChecker.tud.resolver;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.type.Type;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures.CompilationUnitMetaDataContainer;

public class TypeResolver{

    private static final String[] JAVALANG_TYPES = {"String", "Integer", "Float", "Double", "Byte", "Character", "Boolean", "Long", "Short", "Void"};
    private static final String UNKOWN_TYPE = "-UNKOWN_TYPE-";

    /**
     * Given a type and the metadata collected by a former pass, resolve the fully qualified name of a type.
     * For example: A type "ABC" which is imported from "abc.bdf" will be resolved as "abc.bdf.ABC".
     * @param type Type to be resolved using the metadata.
     * @param metaDataContainer Metadata.
     * @return Fully qualified type name.
     */
    public static String resolve(Type type, CompilationUnitMetaDataContainer metaDataContainer){
        List<ImportDeclaration> imports = metaDataContainer.getImports();
        if(type.isPrimitiveType() || type.isVoidType() || TypeResolver.isJavaLangType(type)) {
            return type.toString();
        }
        for(ImportDeclaration importStmt: imports) {
            String[] importStmtParts = importStmt.getName().toString().split("\\.");
            String importedType = importStmtParts[importStmtParts.length - 1];
            if(type.isArrayType()) {
                String importedArrayType = importedType + "[]";
                if(type.toString().equals(importedArrayType))
                    return String.join(".", importStmtParts) + "[]";
            }
            else {
                if(type.toString().equals(importedType))
                    return String.join(".", importedType);
            }
        }
        return TypeResolver.UNKOWN_TYPE;
    }

    private static boolean isJavaLangType(Type type) {
        if(type.isArrayType()) {
            String elementType = type.toString().replace("[]","");
            return Arrays.asList(TypeResolver.JAVALANG_TYPES).contains(elementType);
        }
        else {
            return Arrays.asList(TypeResolver.JAVALANG_TYPES).contains(type.toString());
        }
    }
}
