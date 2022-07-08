package optic_fusion1.kitsune.tool.impl.analyze.analyzer.file;

import java.io.File;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;

public abstract class FileAnalyzer extends Analyzer {

    public abstract void analyze(File file);

}
