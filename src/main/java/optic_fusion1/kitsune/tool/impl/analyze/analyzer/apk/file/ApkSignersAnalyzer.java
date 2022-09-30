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
