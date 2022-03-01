package de.tuda.prg.parser.visitorsje.sgxChecker.tud.substructures;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;

/**
 * This class is used to represent a Field in the Java source-code.
 */
public class Field implements Cloneable{

    private String name;
    private NodeList<Modifier> modifiers;
    private boolean isStatic;
    private String type;
    private boolean isPrimitive;
    private boolean isVoidType;

    public Field() {}

    public Field clone(){
        try {
            super.clone();
        }catch(CloneNotSupportedException e){
            e.getStackTrace();
        }
        Field cloneField = new Field();
        cloneField.setPrimitive(this.isPrimitive());
        cloneField.setName(this.getName());
        cloneField.setType(this.getType());
        cloneField.setIsStatic(this.isStatic());
        cloneField.setModifiers(this.getModifiers());
        cloneField.setVoidType(this.isVoidType());
        return cloneField;
    }

    public String getName() {
        return this.name;
    }

    public NodeList<Modifier> getModifiers() {
        return this.modifiers;
    }

    public String getType() {
        return this.type;
    }

    public String toString() {
        String template = "";
        template += "name: " + this.name + "\n";
        template += "modifiers: " + this.modifiers.toString() + "\n";
        template += "type: " + this.type;
        return template;
    }

    public boolean isStatic(){
        return this.isStatic;
    }

    public void setIsStatic(boolean isStatic){
        this.isStatic = isStatic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModifiers(NodeList<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    public boolean isVoidType() {
        return isVoidType;
    }

    public void setVoidType(boolean voidType) {
        isVoidType = voidType;
    }
}
