package optic_fusion1.kitsune.analyzer.java.code;

import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class InetAddressAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        // TODO: Implement support for getHostName
        if (isMethodInsnNodeCorrect(methodInsnNode, "getLocalHost", "()Ljava/net/InetAddress;")) {
            log(classNode, methodNode, methodInsnNode, tl("iaa_local_host"));
        }
    }
}
