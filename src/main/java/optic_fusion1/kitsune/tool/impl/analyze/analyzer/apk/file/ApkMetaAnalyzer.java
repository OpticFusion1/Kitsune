package optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.file;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.Permission;
import net.dongliu.apk.parser.bean.UseFeature;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.apk.ApkAnalyzer;

public class ApkMetaAnalyzer extends ApkAnalyzer {

    @Override
    public void analyze(ApkFile apkFile) {
        LOGGER.info("Analyzing ApkMeta");
        try {
            ApkMeta apkMeta = apkFile.getApkMeta();
            LOGGER.info("\n" + apkMeta.toString());

            LOGGER.info("Permissions:");
            for (Permission perm : apkMeta.getPermissions()) {
                LOGGER.info(perm.toString());
            }

            LOGGER.info("Uses Features:");
            for (String feature : apkMeta.getUsesPermissions()) {
                LOGGER.info(feature);
            }

            LOGGER.info("Uses Permissions:");
            for (UseFeature useFeature : apkMeta.getUsesFeatures()) {
                LOGGER.info(useFeature.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(ApkMetaAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
