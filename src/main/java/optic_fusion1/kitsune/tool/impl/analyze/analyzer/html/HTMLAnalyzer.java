package optic_fusion1.kitsune.tool.impl.analyze.analyzer.html;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags.AElementAnalyzer;
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

    @Override
    public void analyze(File input) {
        try {
            Document document = Jsoup.parse(input, "utf-8");
            Elements scriptElements = document.select("script");
            if (scriptElements != null) {
                new ScriptElementAnalyzer().analyze(scriptElements);
            }
            Elements aElements = document.select("a");
            if (aElements != null) {
                new AElementAnalyzer().analyze(aElements);
            }
            Elements linkElements = document.select("link");
            if (linkElements != null) {
                new LinkElementAnalyzer().analyze(linkElements);
            }
            Elements iframeElements = document.select("iframe");
            if (iframeElements != null) {
                new IFrameElementAnalyzer().analyze(iframeElements);
            }

            LOGGER.info("Logging comments");
            for (Element element : document.getAllElements()) {
                for (Node node : element.childNodes()) {
                    if (node instanceof Comment) {
                        System.out.println(node);
                    }
                }
            }
            LOGGER.info("\t");

        } catch (IOException ex) {
            Logger.getLogger(HTMLAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
