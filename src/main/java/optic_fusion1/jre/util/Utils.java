package optic_fusion1.jre.util;

import static optic_fusion1.jre.util.Validate.isNotNull;
import org.objectweb.asm.Type;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public final class Utils {

    private Utils() {

    }

    public static boolean checkFileExists(File file) {
        isNotNull(file);
        return file.exists();
    }

    public String getMethodReturnTypes(final String desc) {
        final Type[] types = Type.getArgumentTypes(desc);
        final List<String> strTypes = new LinkedList<String>();
        for (final Type type : types) {
            strTypes.add(type.getClassName());
        }
        return String.join(", ", strTypes);
    }

}
