package optic_fusion1.kitsune.tool.impl;

import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.I18n.tl;
import optic_fusion1.kitsune.util.Utils;
import org.apache.commons.codec.digest.DigestUtils;

public class NormalizeTool extends Tool {

    private boolean showSha1Hash;

    public NormalizeTool() {
        super("normalize", tl("nt_desc"));
    }

    @Override
    public void run(List<String> args) {
        // TODO: Add better arg handling
        if (args.isEmpty()) {
            LOGGER.info(tl("not_enough_args") + " " + tl("nt_usage"));
            return;
        }
        if (args.contains("--showSHA1Hash")) {
            args.remove("--showSHA1Hash");
            showSha1Hash = true;
        }
        String string = Utils.normalize(args.get(0));
        String sha1Hash = "";
        if (showSha1Hash) {
            sha1Hash = DigestUtils.sha1Hex(string);
        }
        LOGGER.info(string + (showSha1Hash ? ": " + sha1Hash : ""));
    }

}
