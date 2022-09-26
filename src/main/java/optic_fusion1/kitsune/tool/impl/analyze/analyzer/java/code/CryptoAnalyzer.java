package optic_fusion1.kitsune.tool.impl.analyze.analyzer.java.code;

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CryptoAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/io/OutputStream;Ljavax/crypto/Cipher;)V")) {
            LOGGER.info("Initializes a CipherOutputStream");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getInstance", "(Ljava/lang/String;)Ljavax/crypto/Cipher;")) {
            AbstractInsnNode previous = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(previous)) {
                LOGGER.info("Creates a Cipher instance");
                return;
            }
            LOGGER.info("Creates a Cipher instance with the transformation " + (String) ((LdcInsnNode) previous).cst);
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "([BLjava/lang/String;)V")) {
            LOGGER.info("Initializes a SecretKeySpec");
            AbstractInsnNode previous = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(previous)) {
                LOGGER.info("Creates a SecretKeySpec instance");
                return;
            }
            LOGGER.info("Creates a SecretKeySpec with the algorithm " + (String) ((LdcInsnNode) previous).cst);
            return;
        }
    }

}
