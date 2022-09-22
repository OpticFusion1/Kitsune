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
package optic_fusion1.kitsune.tool.impl.analyze.analyzer.java.code;

import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class FilesCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "copy", "(Ljava/io/InputStream;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)J")) {
            log(classNode, methodNode, methodInsnNode, tl("fsca_file_copy"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createsDirectories", "(Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;")) {
            log(classNode, methodNode, methodInsnNode, tl("fsca_creates_directories"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createFile", "(Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;")) {
            log(classNode, methodNode, methodInsnNode, tl("fsca_creates_file"));
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "setAttribute",
                "(Ljava/nio/file/Path;Ljava/lang/String;Ljava/lang/Object;[Ljava/nio/file/LinkOption;)Ljava/nio/file/Path;")) {
            AbstractInsnNode minus5 = methodInsnNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious();
            if (!(isAbstractNodeString(minus5))) {
                log(classNode, methodNode, methodInsnNode, tl("fsca_file_attribute_set"));
            }
            String attribute = (String) ((LdcInsnNode) minus5).cst;
            log(classNode, methodNode, methodInsnNode, tl("fsca_known_file_attribute_set", attribute));
        }
    }

}
