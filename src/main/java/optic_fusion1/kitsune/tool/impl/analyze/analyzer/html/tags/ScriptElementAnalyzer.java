package optic_fusion1.kitsune.tool.impl.analyze.analyzer.html.tags;

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScriptElementAnalyzer extends HTMLElementAnalyzer {

    @Override
    public void analyze(Elements elements) {
        LOGGER.info("Analyzing script elements");
        for (Element element : elements) {
            String code = element.html();
            String language = element.hasAttr("language") ? element.attr("language") : "";
            String type = element.hasAttr("type") ? element.attr("type") : "";
            String source = element.hasAttr("src") ? element.attr("src") : "";
            String nonce = element.hasAttr("nonce") ? element.attr("nonce") : "";
            StringBuilder builder = new StringBuilder();
            if (!language.isBlank()) {
                builder.append("Language: ").append(language).append(" ");
            }
            if (!source.isBlank()) {
                builder.append("Source: ").append(source).append(" ");
            }
            if (!type.isBlank()) {
                builder.append("Type: ").append(type).append(" ");
            }
            if (!code.isBlank()) {
                builder.append("Code: ").append(code).append(" ");
            }
            if (!nonce.isBlank()) {
                builder.append("Nonce: ").append(nonce).append(" ");
            }
            LOGGER.info(builder.toString());
        }
        LOGGER.info("\t");
    }

}
