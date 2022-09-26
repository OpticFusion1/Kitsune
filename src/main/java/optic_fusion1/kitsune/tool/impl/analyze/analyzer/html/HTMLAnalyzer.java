package optic_fusion1.kitsune.tool.impl.analyze.analyzer.html;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags.AElementAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags.HTMLElementAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags.IFrameElementAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags.LinkElementAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags.ScriptElementAnalyzer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class HTMLAnalyzer extends Analyzer {

    private static final HashMap<String, HTMLElementAnalyzer> ANALYZERS = new HashMap<>();

    public HTMLAnalyzer() {
        ANALYZERS.put("script", new ScriptElementAnalyzer());
        ANALYZERS.put("a", new AElementAnalyzer());
        ANALYZERS.put("link", new LinkElementAnalyzer());
        ANALYZERS.put("iframe", new IFrameElementAnalyzer());
    }

    @Override
    public void analyze(File input) {
        try {
            Document document = Jsoup.parse(input, "utf-8");
            for (Map.Entry<String, HTMLElementAnalyzer> entry : ANALYZERS.entrySet()) {
                Elements elements = document.select(entry.getKey());
                if (elements != null) {
                    entry.getValue().analyze(elements);
                }
            }
            LOGGER.info("Logging comments");
            for (Element element : document.getAllElements()) {
                for (Node node : element.childNodes()) {
                    if (node instanceof Comment) {
                        System.out.println(node); // TODO: Convert this to use LOGGER
                    }
                }
            }
            LOGGER.info("\t");
        } catch (IOException ex) {
            LOGGER.exception(ex);
        }
    }

}
