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
package optic_fusion1.kitsune.analyzer.java.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import static optic_fusion1.kitsune.util.I18n.tl;

public class ManifestMFFileAnalyzer extends FileAnalyzer {

    @Override
    public void analyze(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry("META-INF/MANIFEST.MF");
            if (entry == null) {
                LOGGER.info(tl("mf_file_not_found", file.toPath()));
                return;
            }
            InputStream inputStream = zipFile.getInputStream(entry);
            String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            LOGGER.info(result);
        } catch (IOException ex) {
            Logger.getLogger(ManifestMFFileAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
