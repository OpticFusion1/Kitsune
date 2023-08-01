package optic_fusion1.kitsune.analyzer.java.code;

import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SocketCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!(minus1 instanceof LdcInsnNode)) {
                log(classNode, methodNode, methodInsnNode, tl("sca_socket_created"));
                return;
            }
            String socketName = (String) ((LdcInsnNode) minus1).cst;
            log(classNode, methodNode, methodInsnNode, tl("sca_host_socket_created", socketName));
            return;
        }
    }

}
