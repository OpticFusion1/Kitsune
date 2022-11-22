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

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CryptoAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "(Ljava/io/OutputStream;Ljavax/crypto/Cipher;)V")) {
            LOGGER.info("Initializes a CipherOutputStream");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "getInstance", "(Ljava/lang/String;)Ljavax/crypto/Cipher;")) {
            AbstractInsnNode previous = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(previous)) {
                LOGGER.info("Creates a Cipher instance");
                return;
            }
            LOGGER.info("Creates a Cipher instance with the transformation " + (String) ((LdcInsnNode) previous).cst);
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "([BLjava/lang/String;)V")) {
            LOGGER.info("Initializes a SecretKeySpec");
            AbstractInsnNode previous = methodInsnNode.getPrevious();
            if (!isAbstractNodeString(previous)) {
                LOGGER.info("Creates a SecretKeySpec instance");
                return;
            }
            LOGGER.info("Creates a SecretKeySpec with the algorithm " + (String) ((LdcInsnNode) previous).cst);
            return;
        }
    }

}
