package optic_fusion1.jre.tool.impl.analyze.analyzer;

import static optic_fusion1.jre.util.Utils.log;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class StreamAnalyzer extends Analyzer {

    private String streamName;

    public StreamAnalyzer(String streamName) {
        this.streamName = streamName;
    }

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/io/File;)V")) {
            log(classNode, methodNode, methodInsnNode, "Creates a " + streamName);
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/io/InputStream;)V")) {
            log(classNode, methodNode, methodInsnNode, "Creates a " + streamName);
        }
    }

}
