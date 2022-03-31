package optic_fusion1.jre.tool.impl.analyze.analyzer;

import static optic_fusion1.jre.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SQLAnalyzer extends Analyzer {

    private String name;

    public SQLAnalyzer(String name) {
        this.name = name;
    }

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "prepareStatement", "(Ljava/lang/String;)Ljava/sql/PreparedStatement;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (minus1 instanceof LdcInsnNode ldc) {
                if (ldc.cst instanceof String statement) {
                    log(classNode, methodNode, methodInsnNode, "Creates the PrepatedStatement '" + statement + "'");
                } else {
                    log(classNode, methodNode, methodInsnNode, "Creates a PreparedStatement");
                }
            } else {
                log(classNode, methodNode, methodInsnNode, "Creates a PreparedStatement");
            }
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getConnection", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/Connection;")) {
            log(classNode, methodNode, methodInsnNode, "Gets a SQL connection");
        }
    }

}
