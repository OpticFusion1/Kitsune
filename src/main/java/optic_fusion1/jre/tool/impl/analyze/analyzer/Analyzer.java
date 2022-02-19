package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Analyzer {

    public abstract void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode);

    public void log(ClassNode classNode, MethodNode methodNode, MethodInsnNode method, String type){
        if (method.getPrevious() == null || method.getPrevious().getPrevious() == null) {
            return;
        }
        AbstractInsnNode minus2 = method.getPrevious().getPrevious();
        System.out.println(type + ": Class Path: " + classNode.name + "#" + methodNode.name);
    }

    public boolean isAbstractNodeString(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcIsnNode)) {
            return false;
        }
        if (!(ldcIsnNode.cst instanceof String)) {
            return false;
        }
        return true;
    }


}
