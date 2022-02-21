package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class FileAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;)V")
                || isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/io/File;Ljava/lang/String;)V")) {
            log(classNode, methodNode, methodInsnNode, "File Initialized");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "mkdir", "()Z") || isMethodInsnNodeCorrect(methodInsnNode, "mkdirs", "()Z")) {
            log(classNode, methodNode, methodInsnNode, "Directory Created");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "delete", "()Z")) {
            log(classNode, methodNode, methodInsnNode, "File Deleted");
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createTempFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            AbstractInsnNode minus2 = minus1.getPrevious();
            if (isAbstractNodeString(minus1) && isAbstractNodeString(minus2)) {
                String fileExtension = (String) ((LdcInsnNode) minus1).cst;
                String fileName = (String) ((LdcInsnNode) minus2).cst;
                log(classNode, methodNode, methodInsnNode, "Creates the tempFile '" + fileName + fileExtension + "'");
                return;
            }
            log(classNode, methodNode, methodInsnNode, "Temp File Created");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createNewFile", "()Z")) {
            log(classNode, methodNode, methodInsnNode, "File Created");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "renameTo", "(Ljava/io/File;)Z")) {
            log(classNode, methodNode, methodInsnNode, "File Renamed");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "delete", "()Z") || isMethodInsnNodeCorrect(methodInsnNode, "deleteOnExit", "()Z")) {
            log(classNode, methodNode, methodInsnNode, "File Deleted");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "list", "()[Ljava/lang/String;")
        || isMethodInsnNodeCorrect(methodInsnNode, "listFiles", "()[Ljava/io/File;")
        || isMethodInsnNodeCorrect(methodInsnNode, "listRoots", "()[Ljava/io/File;")) {
            log(classNode, methodNode, methodInsnNode, "Lists Files");
            return;
        }
    }

}
