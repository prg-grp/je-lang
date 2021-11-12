package de.tuda.prg.taskprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.SwitchEntry.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import org.apache.commons.io.FilenameUtils;

import de.tuda.prg.constants.FileNames;
import de.tuda.prg.filehandling.FileUtils;

public class JARSeperator {//extends VoidVisitorAdapter<Boolean> { 

    private ArrayList<String> files;
    private ArrayList<String> enclaveJAR;
    private ArrayList<String> nonEnclaveJAR;
    private ArrayList<String> bothJar;


    public JARSeperator() {
        files = new ArrayList<>();
        enclaveJAR = new ArrayList<>();
        nonEnclaveJAR = new ArrayList<>();
        bothJar = new ArrayList<>();
    }

    /*public void visit(ClassOrInterfaceDeclaration corid, Boolean isEnclave) {
        String name = corid.getNameAsString();
        System.out.println("NAME OF DECLARATION IS : "+name);
        if (files.contains(name)) {
            if (isEnclave) {
                if (!enclaveJAR.contains(name)) {
                    enclaveJAR.add(name);
                }
            } else {
                if (!nonEnclaveJAR.contains(name)) {
                    nonEnclaveJAR.add(name);
                }
            }
        }
        super.visit(corid, isEnclave);
    }

    public void visit(MethodCallExpr mce, Boolean isEnclave) {
        String name = mce.getScope().isPresent() ? mce.getScope().get().toString() : mce.toString();
        System.out.println("NAME OF DECLARATION IS : "+name);
        if (files.contains(name)) {
            if (isEnclave) {
                if (!enclaveJAR.contains(name)) {
                    enclaveJAR.add(name);
                }
            } else {
                if (!nonEnclaveJAR.contains(name)) {
                    nonEnclaveJAR.add(name);
                }
            }
        }
        super.visit(mce, isEnclave);
    }

    public void visit(ObjectCreationExpr oce, Boolean isEnclave) {
        String name = oce.getTypeAsString();
        System.out.println("NAME OF DECLARATION IS : "+name);
        if (files.contains(name)) {
            if (isEnclave) {
                if (!enclaveJAR.contains(name)) {
                    enclaveJAR.add(name);
                }
            } else {
                if (!nonEnclaveJAR.contains(name)) {
                    nonEnclaveJAR.add(name);
                }
            }
        }
        super.visit(oce, isEnclave);
    }*/




    public void process(final File generetedJavaDir) {
        try {
            File[] directoryListing = generetedJavaDir.listFiles();
            if (directoryListing != null) {
                for (File file : directoryListing) {
                    String currentFileName = file.getName();
                    System.out.println("Currently regarding : " + currentFileName);
                    String currentFileBaseName = FilenameUtils.removeExtension(currentFileName);
                    files.add(currentFileBaseName);
                }
            }
            System.out.println(files);
            System.out.println("-------------ENCLAVE-----------");
            seperate(generetedJavaDir, true);
            System.out.println(enclaveJAR);
            System.out.println("-------------ENCLAVE-----------");
            System.out.println("-------------NONENCLAVE-----------");
            seperate(generetedJavaDir, false);
            System.out.println(nonEnclaveJAR);
            System.out.println("-------------NONENCLAVE-----------");
            findboth();
            System.out.println(bothJar);
            move(generetedJavaDir);
        } catch (Exception e) {
            
        }
    }

    /*private void seperate(final File generetedJavaDir, boolean enclave) {
        System.out.println("-------------Started Seperation-----------");
        String name = "";
        if (enclave) name = "EnclaveMainClass";
        else name = "Main";
        File base = find(generetedJavaDir, name);
        try {
            CompilationUnit cu = StaticJavaParser.parse(base);

            if (enclave) enclaveJAR.add(FilenameUtils.removeExtension(base.getName()));
            else nonEnclaveJAR.add(FilenameUtils.removeExtension(base.getName()));

            int oldsize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();

            int i = 0;
            do {
                System.out.println("-------------WHILE LOOP-----------");
                System.out.println("NAME IS : "+name);
                System.out.println(enclaveJAR);
                base = find(generetedJavaDir, name);
                cu = StaticJavaParser.parse(base);
                this.visit(cu, enclave);
                i++;
                name = enclave ? enclaveJAR.get(i) : nonEnclaveJAR.get(i);
            } while (enclave ? i <= enclaveJAR.size() : i <= nonEnclaveJAR.size());

            System.out.println("-------------Ended Seperation-----------");
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT EXISTING!");
        }
        
    }*/

    private void seperate(final File generetedJavaDir, boolean enclave) {
        String name = "";
        if (enclave) name = FileNames.ENCLAVE_MAIN_CLASS_BASE_NAME;
        else if (files.contains(FileNames.NONENCLAVE_MAIN_CLASS_BASE_NAME)) name = FileNames.NONENCLAVE_MAIN_CLASS_BASE_NAME; // This as constants
        else name = FileNames.NON_ENCLAVE_WRAPPER_CLASS_BASE_NAME;
        File base = find(generetedJavaDir, name);
        if (enclave) enclaveJAR.add(FilenameUtils.removeExtension(base.getName()));
        else nonEnclaveJAR.add(FilenameUtils.removeExtension(base.getName()));
        int maxSize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();
        int currentSize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();
        //System.out.println("MAXSIZE : "+maxSize);
        //System.out.println("CurrentSize : "+currentSize);
        try {
            String sourceCode = StaticJavaParser.parse(base).toString();
            while (currentSize<=maxSize) {
                //System.out.println(enclaveJAR);
                //System.out.println(" ");
                //System.out.print(base.getName().toString());
                //System.out.print(" : ");
                for (String fileName : files) {
                    boolean alreadyInList = false;
                    if (enclave && enclaveJAR.contains(fileName)) alreadyInList = true;
                    else if (!enclave && nonEnclaveJAR.contains(fileName)) alreadyInList = true;
                    //else if (!enclave && enclaveJAR.contains(fileName)) alreadyInList = true;
                    if ((sourceCode.contains(" "+fileName) || sourceCode.contains("("+fileName)) && !alreadyInList) {
                        //System.out.print(fileName);
                        //System.out.print(", ");
                        if (enclave) enclaveJAR.add(fileName);
                        else nonEnclaveJAR.add(fileName);
                    }
                }
                maxSize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();
                if (currentSize<maxSize) {
                    //System.out.println(" ");
                    //System.out.println("NAME TO RECEIVE NEXT : "+enclaveJAR.get(currentSize));
                    if (enclave) base = find(generetedJavaDir, enclaveJAR.get(currentSize));
                    else base = find(generetedJavaDir, nonEnclaveJAR.get(currentSize));
                    sourceCode = StaticJavaParser.parse(base).toString();
                }
                currentSize ++;
                //System.out.println("MAXSIZE : "+maxSize);
                //System.out.println("CurrentSize : "+currentSize);
            }

        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!");
        }
    }

    private void findboth() {
        for (String f : enclaveJAR) {
            if (nonEnclaveJAR.contains(f)) bothJar.add(f);
        }
    }



    private void move(final File generatedJavaDir) {
        String enclavePath = generatedJavaDir.getPath()+"/enclaveJar";
        String nonEnclavePath = generatedJavaDir.getPath()+"/nonEnclaveJar";
        File enclaveJARDirectory = new File(enclavePath);
        File nonEnclaveJARDirectory = new File(nonEnclavePath);

        
        enclaveJARDirectory.mkdir();
        nonEnclaveJARDirectory.mkdir();


        for(String fileName : enclaveJAR) {
            if (!bothJar.contains(fileName)) {
                File f = find(generatedJavaDir, fileName);
                f.renameTo(new File(enclavePath+"/"+f.getName()));
            }
        }
        for(String fileName : nonEnclaveJAR) {
            if (!bothJar.contains(fileName)) {
                File f = find(generatedJavaDir, fileName);
                f.renameTo(new File(nonEnclavePath+"/"+f.getName()));
            }
        }
        for(String filename : bothJar) {
            File f = find(generatedJavaDir, filename);
            File f1 = new File(enclavePath+"/"+f.getName());
            File f2 = new File(nonEnclavePath+"/"+f.getName());
            try {
                Files.copy(Paths.get(f.getPath()), Paths.get(f1.getPath()), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(Paths.get(f.getPath()), Paths.get(f2.getPath()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            f.delete();
        }
    }


    private File find(final File generatedJavaDir, String name) {
        File[] directoryListing = generatedJavaDir.listFiles();
        for(File f: directoryListing) {
            //System.out.println(FilenameUtils.removeExtension(f.getName()));
            if (FilenameUtils.removeExtension(f.getName()).equals(name)) return f;
        }
        return null;
    }
}
