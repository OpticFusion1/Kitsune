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
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ThreadCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "interrupt", "()V")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (minus1 instanceof FieldInsnNode) {
                log(classNode, methodNode, methodInsnNode, tl("thread_interrupted"));
                return;
            }
            if (!(minus1 instanceof MethodInsnNode)) {
                log(classNode, methodNode, methodInsnNode, tl("thread_interrupted"));
                return;
            }
            if (isMethodInsnNodeCorrect((MethodInsnNode) minus1, "currentThread", "()Ljava/lang/Thread;")) {
                log(classNode, methodNode, methodInsnNode, tl("current_thread_interrupted"));
                return;
            }
            log(classNode, methodNode, methodInsnNode, tl("thread_interrupted"));
            return;
        }
        if (methodInsnNode.name.equals("sleep")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (isAbstractNodeString(minus1) || isAbstractNodeLong(minus1)) {
                long millis = -1;
                LdcInsnNode ldc = (LdcInsnNode) minus1;
                if (ldc.cst instanceof Long value) {
                    millis = value;
                } else if (ldc.cst instanceof String value) {
                    millis = Long.parseLong(value);
                }
                log(classNode, methodNode, methodInsnNode, tl("thread_sleep_long", millis));
                return;
            }
            log(classNode, methodNode, methodInsnNode, tl("thread_sleep"));
            return;
        }
    }
}
