/*
* Copyright (C) 2022 Optic_Fusion1
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
