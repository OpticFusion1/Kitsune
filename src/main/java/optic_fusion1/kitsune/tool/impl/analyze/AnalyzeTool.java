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

import java.io.File;
import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.analyzer.AnalyzerManager;
import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.checkFileExists;
import org.apache.commons.io.FilenameUtils;

public class AnalyzeTool extends Tool {

    private AnalyzerManager analyzerManager;
    
    public AnalyzeTool(AnalyzerManager analyzerManager) {
        super("analyze", tl("at_desc"));
        this.analyzerManager = analyzerManager;
    }

    @Override
    public void run(List<String> args) {
        if (args.isEmpty()) {
            LOGGER.warn(tl("not_enough_args") + " " + tl("at_usage"));
            return;
        }
        if ("all".equalsIgnoreCase(args.get(0))) {
            handleAll(args);
            return;
        }
        File file = new File(args.get(0));
        LOGGER.info(tl("at_analyzing", file.toPath()));
        handleFile(file);
    }

    private void handleAll(List<String> args) {
        File input = new File(args.get(1));
        if (!checkFileExists(input)) {
            LOGGER.info(tl("file_does_not_exist", input.toPath()));
            return;
        }
        if (!input.isDirectory()) {
            LOGGER.info(tl("file_is_not_directory", input.toPath()));
            return;
        }
        for (File file : input.listFiles()) {
            if (file.isDirectory()) {
                continue;
            }
            LOGGER.info(tl("at_analyzing", file.toPath()));
            if (handleFile(file)) {
                LOGGER.info("\n\n\n\n");
            }
        }
    }

    private boolean handleFile(File input) {
        if (!checkFileExists(input)) {
            LOGGER.info(tl("file_does_not_exist", input.toPath()));
            return false;
        }

        String extension = FilenameUtils.getExtension(input.getName().replaceFirst(".\\\\?./", ""));
        if (extension.isBlank()) {
            return false;
        }
        if (!analyzerManager.supportsExtension(extension)) {
            LOGGER.info(tl("file_unsupported_extension", extension));
            return false;
        }
        analyzerManager.getAnalyzerForExtension(extension).analyze(input);
        return true;
    }

}
