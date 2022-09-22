package optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags;

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AElementAnalyzer extends HTMLElementAnalyzer {

    @Override
    public void analyze(Elements elements) {
        LOGGER.info("Analyzing a elements");
        for (Element element : elements) {
            String title = element.html();
            String url = element.attr("href");
            LOGGER.info("Title: " + title+ " URL: " + url);
        }
        LOGGER.info("\t");
    }

}
