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

public class SQLCodeAnalyzer extends CodeAnalyzer {

    private String name;

    public SQLCodeAnalyzer(String name) {
        this.name = name;
    }

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "prepareStatement", "(Ljava/lang/String;)Ljava/sql/PreparedStatement;")) {
            AbstractInsnNode minus1 = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(minus1)) {
                log(classNode, methodNode, methodInsnNode, tl("sqlca_ps_created"));
                return;
            }
            String statement = (String) ((LdcInsnNode) minus1).cst;
            log(classNode, methodNode, methodInsnNode, tl("sqlca_ps_string_created", statement));
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getConnection", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/sql/Connection;")) {
            log(classNode, methodNode, methodInsnNode, tl("sqlca_ps_connection_get"));
            return;
        }
    }

}
