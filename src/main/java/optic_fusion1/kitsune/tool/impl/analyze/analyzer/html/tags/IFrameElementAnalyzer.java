package optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags;

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IFrameElementAnalyzer extends HTMLElementAnalyzer {

    //<iframe src="aaaa" height="0" width="0" style="aaaaa"></iframe>
    @Override
    public void analyze(Elements elements) {
        LOGGER.info("Analyzing IFrame elements");
        for (Element element : elements) {
            String source = element.attr("src");
            double height = Double.parseDouble(element.attr("height"));
            double width = Double.parseDouble(element.attr("width"));
            String style = element.attr("style");
            
            StringBuilder builder = new StringBuilder();
            builder.append("Source: ").append(source).append(" ");
            builder.append("height: ").append(height).append(" ");
            builder.append("width: ").append(width).append(" ");
            builder.append("style: ").append(style).append(" ");
            LOGGER.info(builder.toString());
        }
        LOGGER.info("\t");
    }

}
