package optic_fusion1.jre.tool.impl.analyze.analyzer;

import static optic_fusion1.jre.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ZipEntryAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(minus1)) {
                log(classNode, methodNode, methodInsnNode, "ZipEntry Initialized");
                return;
            }
            String zipEntryName = (String) ((LdcInsnNode) minus1).cst;
            log(classNode, methodNode, methodInsnNode, "Creates a ZipEntry called '" + zipEntryName + "'");
        }
    }
}
