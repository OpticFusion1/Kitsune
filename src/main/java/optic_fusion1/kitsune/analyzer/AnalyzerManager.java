package optic_fusion1.kitsune.analyzer;

import java.util.HashMap;
import optic_fusion1.kitsune.analyzer.html.HTMLAnalyzer;
import optic_fusion1.kitsune.analyzer.java.JavaAnalyzer;

public class AnalyzerManager {

    private static final HashMap<String, Analyzer> ANALYZERS = new HashMap<>();
    private static final JavaAnalyzer JAVA_ANALYZER = new JavaAnalyzer();

    static {
        ANALYZERS.put("java", JAVA_ANALYZER);
        ANALYZERS.put("jar", JAVA_ANALYZER);
        ANALYZERS.put("html", new HTMLAnalyzer());
        ANALYZERS.put("vbs", new VBSAnalyzer());
        ANALYZERS.put("bat", new BatchAnalyzer());
    }

    public boolean supportsExtension(String name) {
        return ANALYZERS.containsKey(name);
    }

    public Analyzer getAnalyzerForExtension(String extension) {
        return ANALYZERS.get(extension);
    }

    public void addAnalyzerForExtension(String extension, Analyzer analyzer) {
        ANALYZERS.putIfAbsent(extension, analyzer);
    }

}
