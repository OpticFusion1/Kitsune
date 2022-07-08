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
package optic_fusion1.kitsune.util;

import static optic_fusion1.kitsune.util.Validate.isNotNull;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import java.io.File;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.ParameterNode;

public final class Utils {

    private Utils() {

    }

    public static boolean checkFileExists(File file) {
        isNotNull(file);
        return file.exists();
    }

    public static void log(ClassNode classNode, FieldNode fieldNode) {
        LOGGER.info("Field: " + classNode.name + "#" + fieldNode.name + " " + fieldNode.desc + " " + fieldNode.signature + " " + fieldNode.value);
    }

    public static void log(ClassNode classNode, MethodNode methodNode, ParameterNode parameterNode) {
        LOGGER.info("Parameter: " + classNode.name + "#" + methodNode.name + ": " + parameterNode.name);
    }

    public static void log(ClassNode classNode, MethodNode methodNode, MethodInsnNode method, String message) {
        if (method.getPrevious() == null || method.getPrevious().getPrevious() == null) {
            return;
        }
        LOGGER.info("Method Insn Node: " + classNode.name + "#" + methodNode.name + ": " + message);
    }

}
