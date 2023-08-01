/*
* Copyright (C) 2022 Optic_Fusion1
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package optic_fusion1.kitsune.analyzer.java.code;

import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class FileCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!(minus1 instanceof LdcInsnNode)) {
                log(classNode, methodNode, methodInsnNode, "File created");
                return;
            }
            String fileName = (String) ((LdcInsnNode) minus1).cst;
            log(classNode, methodNode, methodInsnNode, tl("fca_named_file_created", fileName));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            AbstractInsnNode minus2 = minus1.getPrevious();
            if (!(minus1 instanceof LdcInsnNode) || !(minus2 instanceof LdcInsnNode)) {
                log(classNode, methodNode, methodInsnNode, "File created");
                return;
            }
            String fileName = (String) ((LdcInsnNode) minus1).cst;
            String directoryName = (String) ((LdcInsnNode) minus2).cst;
            log(classNode, methodNode, methodInsnNode, tl("fca_named_file_created_dir", fileName, directoryName));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/lang/String;)V")
                || isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/io/File;Ljava/lang/String;)V")) {
            log(classNode, methodNode, methodInsnNode, tl("fca_file_initialized"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "mkdir", "()Z") || isMethodInsnNodeCorrect(methodInsnNode, "mkdirs", "()Z")) {
            log(classNode, methodNode, methodInsnNode, tl("fca_dir_created"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createTempFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            AbstractInsnNode minus2 = minus1.getPrevious();
            if (isAbstractNodeString(minus1) && isAbstractNodeString(minus2)) {
                String fileExtension = (String) ((LdcInsnNode) minus1).cst;
                String fileName = (String) ((LdcInsnNode) minus2).cst;
                log(classNode, methodNode, methodInsnNode, tl("fca_named_temp_file_created", fileName, fileExtension));
                return;
            }
            log(classNode, methodNode, methodInsnNode, tl("fca_temp_file_created"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createNewFile", "()Z")) {
            log(classNode, methodNode, methodInsnNode, tl("fca_file_created"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "renameTo", "(Ljava/io/File;)Z")) {
            log(classNode, methodNode, methodInsnNode, tl("fca_file_renamed"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "delete", "()Z") || isMethodInsnNodeCorrect(methodInsnNode, "deleteOnExit", "()Z")) {
            log(classNode, methodNode, methodInsnNode, tl("fca_file_deleted"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "list", "()[Ljava/lang/String;")
                || isMethodInsnNodeCorrect(methodInsnNode, "listFiles", "()[Ljava/io/File;")
                || isMethodInsnNodeCorrect(methodInsnNode, "listRoots", "()[Ljava/io/File;")) {
            log(classNode, methodNode, methodInsnNode, tl("fca_lists_files"));
            return;

        }

        if (isMethodInsnNodeCorrect(methodInsnNode, "exists", "()Z")) {
            if (methodInsnNode.getPrevious() instanceof MethodInsnNode minus1) {
                if (isMethodInsnNodeCorrect(minus1, "<init>", "(Ljava/lang/String;)V")) {
                    if (minus1.getPrevious() instanceof LdcInsnNode ldcInsnNode && ldcInsnNode.cst instanceof String string) {
                        log(classNode, methodNode, methodInsnNode, "Checks if the file " + string + " exists");
                        return;
                    }
                }
            }
        }
    }

}
