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
package optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.apk.parser.ApkFile;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file.ApkDexAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file.ApkMetaAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file.ApkSignersAnalyzer;

public class ApkAnalyzerTool extends Analyzer {

    private static final List<ApkAnalyzer> ANALYZERS = new ArrayList<>();

    public ApkAnalyzerTool() {
        ANALYZERS.add(new ApkMetaAnalyzer());
        ANALYZERS.add(new ApkSignersAnalyzer());
        ANALYZERS.add(new ApkDexAnalyzer());
    }

    @Override
    public void analyze(File input) {
        try (ApkFile apkFile = new ApkFile(input)) {
            for (ApkAnalyzer analyzer : ANALYZERS) {
                analyzer.analyze(apkFile);
            }
        } catch (IOException ex) {
            Logger.getLogger(ApkAnalyzerTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
