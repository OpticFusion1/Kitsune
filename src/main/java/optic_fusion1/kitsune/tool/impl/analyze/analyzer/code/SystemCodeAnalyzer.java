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

import static optic_fusion1.kitsune.util.I18n.tl;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SystemCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {

        if (isMethodInsnNodeCorrect(methodInsnNode, "getProperty", "(Ljava/lang/String;)Ljava/lang/String;")) {
            getAndLog(classNode, methodNode, methodInsnNode, tl("sysca_property"));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getenv", "(Ljava/lang/String;)Ljava/lang/String;")) {
            getAndLog(classNode, methodNode, methodInsnNode, tl("sysca_environment"));
        }
    }

    // TODO: Abstract away for other analyzer 
    private void getAndLog(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode, String type) {
        AbstractInsnNode minus1 = methodInsnNode.getPrevious();
        if (!isAbstractNodeString(minus1)) {
            log(classNode, methodNode, methodInsnNode, tl("sysca_get_type"));
            return;
        }
        String varName = (String) ((LdcInsnNode) minus1).cst;
        log(classNode, methodNode, methodInsnNode, tl("sysca_get_type_variable", type, varName));
    }
}
