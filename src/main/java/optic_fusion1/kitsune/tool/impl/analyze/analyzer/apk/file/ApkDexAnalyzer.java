package optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.ApkAnalyzer;

public class ApkDexAnalyzer extends ApkAnalyzer {

    @Override
    public void analyze(ApkFile apkFile) {
        LOGGER.info("Analyzing dex classes");
        try {
            // TODO: Implement support for getting the code itself
            for (DexClass dexClass : apkFile.getDexClasses()) {
                LOGGER.info("Package Name: " + dexClass.getPackageName()
                        + " Super Class: " + dexClass.getSuperClass()
                        + " Class Type:" + dexClass.getClassType());
            }
        } catch (IOException ex) {
            Logger.getLogger(ApkDexAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
