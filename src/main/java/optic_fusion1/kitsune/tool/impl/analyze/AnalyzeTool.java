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
package optic_fusion1.kitsune.tool.impl.analyze;

import optic_fusion1.kitsune.tool.Tool;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.JavaAnalyzer;
import static optic_fusion1.kitsune.util.Utils.checkFileExists;
import java.io.File;
import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;

public class AnalyzeTool extends Tool {

    private static final JavaAnalyzer JAVA_ANALYZER = new JavaAnalyzer();

    public AnalyzeTool() {
        super("analyze", "Analyzes a jar or class and provides a detailed report");
    }

    @Override
    public void run(List<String> args) {
        if (args.isEmpty()) {
            LOGGER.warn("You did not enter enough arguments. Usage: analyze <file>");
            return;
        }
        if (!args.get(0).equalsIgnoreCase("all")) {
            File input = new File(args.get(0));
            if (!checkFileExists(input)) {
                LOGGER.info(input.toPath() + " does not exist");
                return;
            }
            if (!input.getName().endsWith(".jar") && !input.getName().endsWith(".class")) {
                LOGGER.info(input.toPath() + " is not a jar file");
                return;
            }
            if (input.isDirectory()) {
                LOGGER.info(input.toPath() + " is a directory");
                return;
            }
            JAVA_ANALYZER.analyze(input);
            return;
        }
        if (args.size() == 1) {
            LOGGER.info("You did not enter enough arguments. Usage: printStrings all <directory_path>");
            return;
        }
        File input = new File(args.get(1));
        if (!checkFileExists(input)) {
            LOGGER.info(input.toPath() + " does not exist");
            return;
        }
        if (!input.isDirectory()) {
            LOGGER.info(input.toPath() + " is not a directory");
            return;
        }
        for (File file : input.listFiles()) {
            LOGGER.info("\n\n\n\n");
            LOGGER.info("Analyzing " + file.getName() + ":");
            try {
                JAVA_ANALYZER.analyze(file);
            } catch (Exception e) {
                continue;
            }
        }
    }
}
