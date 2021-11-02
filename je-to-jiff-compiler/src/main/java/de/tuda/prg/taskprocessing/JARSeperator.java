package de.tuda.prg.taskprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.github.javaparser.StaticJavaParser;

import org.apache.commons.io.FilenameUtils;

import de.tuda.prg.filehandling.FileUtils;

public class JARSeperator {  

    private ArrayList<String> files;
    private ArrayList<String> enclaveJAR;
    private ArrayList<String> nonEnclaveJAR;


    public JARSeperator() {
        files = new ArrayList<>();
        enclaveJAR = new ArrayList<>();
        nonEnclaveJAR = new ArrayList<>();
    }


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
            System.out.println("-------------ENCLAVE-----------");
            System.out.println("-------------NONENCLAVE-----------");
            seperate(generetedJavaDir, false);
            System.out.println("-------------NONENCLAVE-----------");
            System.out.println(enclaveJAR);
            System.out.println(nonEnclaveJAR);
            move(generetedJavaDir);
        } catch (Exception e) {
            
        }
    }

    private void seperate(final File generetedJavaDir, boolean enclave) {
        String name = "";
        if (enclave) name = "EnclaveMainClass";
        else name = "Main";
        File base = find(generetedJavaDir, name);
        if (enclave) enclaveJAR.add(FilenameUtils.removeExtension(base.getName()));
        else nonEnclaveJAR.add(FilenameUtils.removeExtension(base.getName()));
        int maxSize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();
        int currentSize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();
        try {
            String sourceCode = StaticJavaParser.parse(base).toString();
            while (currentSize<=maxSize) {
                System.out.println(" ");
                System.out.print(base.toString());
                System.out.print(" : ");
                for (String fileName : files) {
                    boolean alreadyInList = false;
                    if (enclave && enclaveJAR.contains(fileName)) alreadyInList = true;
                    else if (!enclave && nonEnclaveJAR.contains(fileName)) alreadyInList = true;
                    else if (!enclave && enclaveJAR.contains(fileName)) alreadyInList = true;
                    if (sourceCode.contains(" "+fileName) && !alreadyInList) {
                        System.out.print(fileName);
                        System.out.print(", ");
                        if (enclave) enclaveJAR.add(fileName);
                        else nonEnclaveJAR.add(fileName);
                    }
                }
                maxSize = enclave ? enclaveJAR.size() : nonEnclaveJAR.size();
                if (currentSize<maxSize) {
                    if (enclave) base = find(generetedJavaDir, enclaveJAR.get(currentSize));
                    else base = find(generetedJavaDir, nonEnclaveJAR.get(currentSize));
                    sourceCode = StaticJavaParser.parse(base).toString();
                }
                currentSize ++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND!");
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
            File f = find(generatedJavaDir, fileName);
            f.renameTo(new File(enclavePath+"/"+f.getName()));
        }
        for(String fileName : nonEnclaveJAR) {
            File f = find(generatedJavaDir, fileName);
            f.renameTo(new File(nonEnclavePath+"/"+f.getName()));
        }
    }


    private File find(final File generetedJavaDir, String name) {
        File[] directoryListing = generetedJavaDir.listFiles();
        for(File f: directoryListing) {
            if (f.getName().contains(name)) return f;
        }
        return null;
    }
}
