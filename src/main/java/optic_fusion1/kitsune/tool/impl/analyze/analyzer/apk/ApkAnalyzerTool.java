package optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.apk.parser.ApkFile;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file.ApkDexAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file.ApkMetaAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file.ApkSignersAnalyzer;

public class ApkAnalyzerTool extends Analyzer {

    private static final List<ApkAnalyzer> ANALYZERS = new ArrayList<>();

    public ApkAnalyzerTool() {
        ANALYZERS.add(new ApkMetaAnalyzer());
        ANALYZERS.add(new ApkSignersAnalyzer());
        ANALYZERS.add(new ApkDexAnalyzer());
    }

    @Override
    public void analyze(File input) {
        try (ApkFile apkFile = new ApkFile(input)) {
            for (ApkAnalyzer analyzer : ANALYZERS) {
                analyzer.analyze(apkFile);
            }
        } catch (IOException ex) {
            Logger.getLogger(ApkAnalyzerTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
