package optic_fusion1.jre.tool.impl.analyze.analyzer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class FileAnalyzer extends Analyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (methodInsnNode.owner.equals("java/io/File") && methodInsnNode.name.equals("createTempFile") && methodInsnNode.desc.equals("(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            AbstractInsnNode minus2 = minus1.getPrevious();
            if (isAbstractNodeString(minus1) && isAbstractNodeString(minus2)) {
                String fileExtension = (String) ((LdcInsnNode) minus1).cst;
                String fileName = (String) ((LdcInsnNode) minus2).cst;
                System.out.println(classNode.name + "#" + methodNode.name + " creates the tempFile '" + fileName + fileExtension + "'");
                return;
            }
            log(classNode, methodNode, methodInsnNode, "Temp File Created");
        }
        if (methodInsnNode.owner.equals("java/io/File") && methodInsnNode.desc.equals("(Ljava/lang/String;Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            AbstractInsnNode minus2 = minus1.getPrevious();
            if (!isAbstractNodeString(minus1) || !isAbstractNodeString(minus2)) {
                log(classNode, methodNode, methodInsnNode, "File Initialization Found");
                return;
            }
            String fileName = (String) ((LdcInsnNode) minus2).cst;
            String fileExtension = (String) ((LdcInsnNode) minus1).cst;
            System.out.println(classNode.name + "#" + methodNode.name + " creates the file '" + fileName + fileExtension + "'");
            return;
        }
        if (methodInsnNode.owner.equals("java/io/File") && methodInsnNode.desc.equals("(Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(minus1)) {
                log(classNode, methodNode, methodInsnNode, "File Initialization Found");
                return;
            }
            String file = (String) ((LdcInsnNode) minus1).cst;
            System.out.println(classNode.name + "#" + methodNode.name + " creates the file '" + file + "'");
        }
        if (methodInsnNode.name.equals("createNewFile") && methodInsnNode.desc.equals("()Z")) {
            log(classNode, methodNode, methodInsnNode, "File Created");
            return;
        }
        if (methodInsnNode.name.equals("renameTo") && methodInsnNode.desc.equals("(Ljava/io/File;)Z")) {
            log(classNode, methodNode, methodInsnNode, "File Renamed");
            return;
        }
        if ((methodInsnNode.name.equals("delete") && methodInsnNode.desc.equals("()Z")) || (methodInsnNode.name.equals("deleteOnExit") && methodInsnNode.desc.equals("()V"))) {
            log(classNode, methodNode, methodInsnNode, "File Deleted");
            return;
        }
        if (methodInsnNode.name.equals("mkdir") || methodInsnNode.name.equals("mkdirs") && methodInsnNode.desc.equals("()Z")) {
            log(classNode, methodNode, methodInsnNode, "Directory Created");
            return;
        }
        if ((methodInsnNode.name.equals("list") && (methodInsnNode.desc.equals("()[Ljava/lang/String;") || methodInsnNode.desc.equals("(Ljava/io/FilenameFilter;)[Ljava/lang/String;"))) || (methodInsnNode.name.equals("listFiles") && (methodInsnNode.desc.equals("()[Ljava/io/File;") || methodInsnNode.desc.equals("(Ljava/io/FileFilter;)[Ljava/io/File;") || methodInsnNode.desc.equals("(Ljava/io/FilenameFilter;)[Ljava/io/File;"))) || (methodInsnNode.name.equals("listRoots") && methodInsnNode.desc.equals("()[Ljava/io/File;"))) {
            log(classNode, methodNode, methodInsnNode, "Lists Files");
            return;
        }
    }

}
