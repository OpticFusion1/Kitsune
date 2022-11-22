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
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkSigner;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.ApkAnalyzer;

public class ApkSignersAnalyzer extends ApkAnalyzer {

    @Override
    public void analyze(ApkFile apkFile) {
        LOGGER.info("Analyzing ApkSigners");
        try {
            for (ApkSigner signer : apkFile.getApkSingers()) {
                LOGGER.info(signer.toString());
            }
            // TODO: Figure out why this throws exceptions
//            for (ApkV2Signer signer : apkFile.getApkV2Singers()) {
//                LOGGER.info(signer.toString());
//            }
        } catch (IOException | CertificateException ex) {
            Logger.getLogger(ApkSignersAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
