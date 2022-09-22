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
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AWTCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "()V")) {
            log(classNode, methodNode, methodInsnNode, tl("awtca_robot_initialized"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createScreenCapture", "(Ljava/awt/Rectangle;)Ljava/awt/image/BufferedImage;")) {
            log(classNode, methodNode, methodInsnNode, tl("awtca_screen_cap_taken"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getDefaultToolkit", "()Ljava/awt/Toolkit;")) {
            log(classNode, methodNode, methodInsnNode, tl("awtca_get_default_toolkit"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getScreenSize", "()Ljava/awt/Dimension;")) {
            log(classNode, methodNode, methodInsnNode, tl("awtca_get_screen_size"));
            return;
        }

    }

}
