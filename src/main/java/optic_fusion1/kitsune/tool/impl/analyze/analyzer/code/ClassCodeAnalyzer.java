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
package optic_fusion1.kitsune.tool.impl.analyze.analyzer.code;

import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassCodeAnalyzer extends CodeAnalyzer {

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
