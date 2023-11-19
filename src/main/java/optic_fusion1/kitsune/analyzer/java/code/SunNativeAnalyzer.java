package optic_fusion1.kitsune.analyzer.java.code;

import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SunNativeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "loadLibrary", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;")) {
            AbstractInsnNode previous = methodInsnNode.getPrevious();
            Type classPath = previous instanceof LdcInsnNode ? (Type) ((LdcInsnNode) previous).cst : null;
            String dllPath = previous.getPrevious() instanceof LdcInsnNode ? (String) ((LdcInsnNode) previous.getPrevious()).cst : "";
            if (classPath != null && !dllPath.isEmpty()) {
                log(classNode, methodNode, methodInsnNode, tl("sna_load_library_defined", dllPath, classPath));
            } else {
                log(classNode, methodNode, methodInsnNode, tl("sna_load_library"));
            }
        }
    }

}
