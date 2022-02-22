package de.tuda.prg.parser.visitorsje.sgxChecker.tud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    /**
     * Entrypoint for analyzing Class-Files/a Project whether they violate rules of enclaving/secret fields
     * @param args Path to the sgxif.config.json
     */
    public static void main( String[] args ) {
        try {
            logger.trace("Starting SGXChecker");
            SGXChecker sgxChecker = new SGXChecker(args[0]);
            logger.trace("Analyze Class-Files");
            sgxChecker.analyze();
        }catch(ArrayIndexOutOfBoundsException e){
            logger.fatal("Need path to sgxif.config.json");
            System.exit(-1);
        }
    }
}
