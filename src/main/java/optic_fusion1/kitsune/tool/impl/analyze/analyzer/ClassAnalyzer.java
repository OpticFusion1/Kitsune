package optic_fusion1.kitsune.tool.impl.analyze.analyzer;

import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;")) {
            AbstractInsnNode minus3 = methodInsnNode.getPrevious().getPrevious().getPrevious();
            if (minus3 instanceof LdcInsnNode ldc) {
                if (ldc.cst instanceof String string) {
                    log(classNode, methodNode, methodInsnNode, "Gets the Class method '" + string + "'");
                } else {
                    log(classNode, methodNode, methodInsnNode, "Gets a Class method");
                }
            } else {
                log(classNode, methodNode, methodInsnNode, "Gets a Class method");
            }
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (minus1 instanceof LdcInsnNode ldc) {
                if (ldc.cst instanceof String string) {
                    log(classNode, methodNode, methodInsnNode, "Gets a Class field '" + string + "'");
                } else {
                    log(classNode, methodNode, methodInsnNode, "Gets a Class field");
                }
            } else {
                log(classNode, methodNode, methodInsnNode, "Gets a Class field");
            }
        }
    }

}
