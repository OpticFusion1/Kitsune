package optic_fusion1.kitsune.tool.impl.analyze.analyzer.code;

import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class JNativeHookAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "removeNativeKeyListener", "(Lcom/github/kwhat/jnativehook/keyboard/NativeKeyListener;)V")) {
            log(classNode, methodNode, methodInsnNode, "Removes native key listener");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getKeyCode", "()I")) {
            log(classNode, methodNode, methodInsnNode, "Gets NativeKeyEvent KeyCode");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getKeyText", "(I)Ljava/lang/String;")) {
            log(classNode, methodNode, methodInsnNode, "Gets NativeKeyEvent KeyText");
        }
    }

}
