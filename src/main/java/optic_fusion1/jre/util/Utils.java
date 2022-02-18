package optic_fusion1.jre.util;

import static optic_fusion1.jre.util.Validate.isNotNull;
import java.io.File;

public final class Utils {

    private Utils() {

    }

    public static boolean checkFileExists(File file) {
        isNotNull(file);
        return file.exists();
    }

}
