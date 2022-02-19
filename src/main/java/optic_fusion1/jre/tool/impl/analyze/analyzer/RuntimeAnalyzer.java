package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class RuntimeAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (methodInsnNode.name.equals("exec") && methodInsnNode.desc.equals("(Ljava/lang/String;)Ljava/lang/Process;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!(isAbstractNodeString(minus1))) {
                log(classNode, methodNode, methodInsnNode, "Runtime Exec");
                return;
            }
            String execCommand = (String) ((LdcInsnNode) minus1).cst;
            System.out.println(classNode.name + "#" + methodNode.name + " runs the exec command '" + execCommand + "'");
        }
    }

}
