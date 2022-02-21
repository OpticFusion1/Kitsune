package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class URLAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(minus1)) {
                log(classNode, methodNode, methodInsnNode, "URL Created");
                return;
            }
            String url = (String) ((LdcInsnNode) minus1).cst;
            log(classNode, methodNode, methodInsnNode, "Created a URL pointing to '" + url + "'");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "openConnection", "()Ljava/net/URLConnection;")) {
            log(classNode, methodNode, methodInsnNode, "URL Connection Opened");
            return;
        }
    }

}
