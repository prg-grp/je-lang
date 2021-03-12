package de.tuda.prg.filehandling;

import de.tuda.prg.exceptions.FileIOException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static void writeStringToFile(final String fileName, final String data) throws FileNotFoundException, IOException, FileIOException {
        FileOutputStream outputStream = null;
        if (fileName != null && !fileName.isEmpty()) {
            outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = data.getBytes();
            outputStream.write(strToBytes);
        } else {
            System.out.println("File name null or empty.");
            throw new FileIOException(fileName, "File name null or empty.");
        }
    }
}
