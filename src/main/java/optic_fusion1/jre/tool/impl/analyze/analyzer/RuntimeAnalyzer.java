package optic_fusion1.jre.tool.impl.analyze.analyzer;

import static optic_fusion1.jre.util.Utils.log;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class RuntimeAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "exec", "(Ljava/lang/String;)Ljava/lang/Process;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(minus1)) {
                log(classNode, methodNode, methodInsnNode, "Runtime Exec");
                return;
            }
            String execCommand = (String) ((LdcInsnNode) minus1).cst;
            log(classNode, methodNode, methodInsnNode, "Runs the exec command '" + execCommand + "'");
        }
    }

}
