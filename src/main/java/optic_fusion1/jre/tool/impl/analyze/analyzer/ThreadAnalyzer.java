package optic_fusion1.jre.tool.impl.analyze.analyzer;

import static optic_fusion1.jre.util.Utils.log;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ThreadAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (methodInsnNode.name.equals("sleep")) {
            log(classNode, methodNode, methodInsnNode, "Thread Sleep");
        }
    }
}
