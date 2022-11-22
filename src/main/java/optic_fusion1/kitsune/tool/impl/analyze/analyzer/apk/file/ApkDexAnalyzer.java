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
package optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.ApkAnalyzer;

public class ApkDexAnalyzer extends ApkAnalyzer {

    @Override
    public void analyze(ApkFile apkFile) {
        LOGGER.info("Analyzing dex classes");
        try {
            // TODO: Implement support for getting the code itself
            for (DexClass dexClass : apkFile.getDexClasses()) {
                LOGGER.info("Package Name: " + dexClass.getPackageName()
                        + " Super Class: " + dexClass.getSuperClass()
                        + " Class Type:" + dexClass.getClassType());
            }
        } catch (IOException ex) {
            Logger.getLogger(ApkDexAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
