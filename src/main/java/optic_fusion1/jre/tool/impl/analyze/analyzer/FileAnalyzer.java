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
package optic_fusion1.jre.tool.impl.analyze.analyzer;

import static optic_fusion1.jre.util.Utils.log;
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
