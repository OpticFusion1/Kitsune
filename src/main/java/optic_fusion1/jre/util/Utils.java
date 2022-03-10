package optic_fusion1.jre.util;

import static optic_fusion1.jre.util.Validate.isNotNull;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import java.io.File;

public final class Utils {

    private Utils() {

    }

    public static boolean checkFileExists(File file) {
        isNotNull(file);
        return file.exists();
    }

    public static void log(ClassNode classNode, MethodNode methodNode, MethodInsnNode method, String type) {
        if (method.getPrevious() == null || method.getPrevious().getPrevious() == null) {
            return;
        }
        AbstractInsnNode minus2 = method.getPrevious().getPrevious();
        System.out.println(classNode.name + "#" + methodNode.name + ": " + type);
    }

}
