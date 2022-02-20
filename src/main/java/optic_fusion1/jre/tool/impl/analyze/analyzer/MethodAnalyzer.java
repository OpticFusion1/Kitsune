package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodAnalyzer extends Analyzer{

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (methodInsnNode.name.equals("invoke") && methodInsnNode.desc.equals("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;")) {
            log(classNode, methodNode, methodInsnNode, "Method#invoke");
        }
    }

}
