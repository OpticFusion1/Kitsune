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
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class JNativeHookAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "removeNativeKeyListener", "(Lcom/github/kwhat/jnativehook/keyboard/NativeKeyListener;)V")) {
            log(classNode, methodNode, methodInsnNode, tl("jnha_nkl_removed"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getKeyCode", "()I")) {
            log(classNode, methodNode, methodInsnNode, tl("jnha_get_nke_keycode"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getKeyText", "(I)Ljava/lang/String;")) {
            log(classNode, methodNode, methodInsnNode, tl("jnha_get_nke_keytext"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "addNativeKeyListener", "(Lcom/github/kwhat/jnativehook/keyboard/NativeKeyListener;)V")) {
            log(classNode, methodNode, methodInsnNode, tl("jnha_nkl_added"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getRawCode", "()I")) {
            log(classNode, methodNode, methodInsnNode, tl("jnha_get_nke_rawcode"));
            return;
        }
    }

}
