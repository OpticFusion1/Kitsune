package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ZipEntryAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (methodInsnNode.name.equals("<init>") && methodInsnNode.desc.equals("(Ljava/lang/String;)V")) {
            AbstractInsnNode minus = methodInsnNode.getPrevious();
            if (!(minus instanceof LdcInsnNode ldcInsnNode)) {
                log(classNode, methodNode, methodInsnNode, "ZipEntry Initialized");
                return;
            }
            if (!(ldcInsnNode.cst instanceof String string)) {
                log(classNode, methodNode, methodInsnNode, "ZipEntry Initialized");
                return;
            }
            System.out.println(classNode.name + "#" + methodNode.name + " create a ZipEntry called '" + string + "'");
        }
    }
}
