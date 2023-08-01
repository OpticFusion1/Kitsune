package optic_fusion1.kitsune.analyzer.java.code;

import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SystemHookCodeAnalyzer extends CodeAnalyzer {

    // TODO: See if "lc/kra/system/keyboard/GlobalKeyboardHook.shutdownHook()V" is worth adding
    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Z)V")) {
            log(classNode, methodNode, methodInsnNode, "Creates a new SystemHook GlobalKeyboardHook");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "addKeyListener", "(Llc/kra/system/keyboard/event/GlobalKeyListener;)V")) {
            log(classNode, methodNode, methodInsnNode, "Adds a new SystemHook KeyListener");
            return;
        }
    }

}
