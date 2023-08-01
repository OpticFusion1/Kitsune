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

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class CodeAnalyzer {

    public abstract void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode);

    public boolean isMethodInsnNodeCorrect(MethodInsnNode methodInsnNode, String name, String desc) {
        return methodInsnNode.name.equals(name) && methodInsnNode.desc.equals(desc);
    }

    public boolean isAbstractNodeLong(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof Long;
    }

    public boolean isAbstractNodeString(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof String;
    }

}
