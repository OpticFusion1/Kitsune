package optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags;

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkElementAnalyzer extends HTMLElementAnalyzer {

    @Override
    public void analyze(Elements elements) {
        LOGGER.info("Analyzing link elements");
        for (Element element : elements) {
            //<link rel="stylesheet" href="aaaaa">
            String rel = element.attr("rel");
            String href = element.attr("href");
            LOGGER.info("Rel: " + rel + " href: " + href);
        }
        LOGGER.info("\t");
    }

}
