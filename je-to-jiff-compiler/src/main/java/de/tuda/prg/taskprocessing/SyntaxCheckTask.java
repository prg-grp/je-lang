package de.tuda.prg.taskprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import de.tuda.prg.parser.visitorsje.sgxChecker.tud.SGXChecker;

public class SyntaxCheckTask implements CodeXformationTask {

    @Override
    public void run(File jeSrcDir, GlobalTaskData data) {
        System.out.println("Task processing started : Task name: SyntaxCheckTask.");
        System.out.println("Any warnings will be logged: ");
        SGXChecker sgxChecker = new SGXChecker(jeSrcDir.toString());
        sgxChecker.analyze();
    }
}
