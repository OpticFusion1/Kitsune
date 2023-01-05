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

public class AElementAnalyzer extends HTMLElementAnalyzer {

    @Override
    public void analyze(Elements elements) {
        LOGGER.info("Analyzing a elements");
        for (Element element : elements) {
            String title = element.html();
            String url = element.attr("href");
            LOGGER.info("Title: " + title + " URL: " + url);
        }
        LOGGER.info("\t");
    }

}
