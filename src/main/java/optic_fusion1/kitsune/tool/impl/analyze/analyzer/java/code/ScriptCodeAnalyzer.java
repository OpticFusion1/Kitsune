package optic_fusion1.kitsune.tool.impl.analyze.analyzer.java.code;

import optic_fusion1.kitsune.util.Utils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ScriptCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "getEngineByName", "(Ljava/lang/String;)Ljavax/script/ScriptEngine;")) {
            Utils.log(classNode, methodNode, methodInsnNode, "Gets a ScriptEngine instance");
            return;
        }
    }

}
