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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.coley.cafedude.InvalidClassException;
import me.coley.cafedude.classfile.ClassFile;
import me.coley.cafedude.io.ClassFileReader;
import me.coley.cafedude.io.ClassFileWriter;
import me.coley.cafedude.transform.IllegalStrippingTransformer;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.Main;
import optic_fusion1.kitsune.tool.impl.StringsTool;
import static optic_fusion1.kitsune.util.I18n.tl;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.ParameterNode;

public final class Utils {

    private Utils() {

    }

    public static String normalize(String string) {
        return string.toLowerCase().trim();
    }

    public static boolean checkFileExists(File file) {
        isNotNull(file);
        return file.exists();
    }

    public static void log(ClassNode classNode, FieldNode fieldNode) {
        LOGGER.info(tl("utils_log_field", classNode.name, fieldNode.name, fieldNode.desc, fieldNode.signature, fieldNode.value));
    }

    public static void log(ClassNode classNode, MethodNode methodNode, ParameterNode parameterNode) {
        LOGGER.info(tl("utils_log_param", classNode.name, methodNode.name, parameterNode.name));
    }

    public static void log(ClassNode classNode, MethodNode methodNode, MethodInsnNode method, String message) {
        if (method.getPrevious() == null || method.getPrevious().getPrevious() == null) {
            return;
        }
        LOGGER.info(tl("utils_log_min", classNode.name, methodNode.name, message));
    }

    public static InputStream getResource(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException(I18n.tl("null_or_empty", "fileName"));
        }
        InputStream input = Main.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            LOGGER.warn(I18n.tl("resource_not_found", filename));
            return null;
        }
        return input;
    }

    public static void writeToFile(OutputStream outputStream, InputStream inputStream) throws Throwable {
        byte[] buffer = new byte[4096];
        while (inputStream.available() > 0) {
            int data = inputStream.read(buffer);
            outputStream.write(buffer, 0, data);
        }
    }

    public static ClassWriter stripIllegalAttributesAndData(byte[] data) throws InvalidClassException {
        ClassFileReader cfr = new ClassFileReader();
        cfr.setDropForwardVersioned(true);
        cfr.setDropEofAttributes(true);
        ClassFile cf = cfr.read(data);
        new IllegalStrippingTransformer(cf).transform();
        ClassFileWriter cfw = new ClassFileWriter();
        data = cfw.write(cf);
        ClassReader reader = new ClassReader(data);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        ClassWriter classWriter = new ClassWriter(0);
        node.accept(classWriter);
        return classWriter;
    }

    public static ClassNode getClassNodeFromClassFile(File file) {
        if (!file.getName().endsWith(".class")) {
            return null;
        }
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ClassReader classReader;
            try {
                classReader = new ClassReader(inputStream);
            } catch (IOException e) {
                return null;
            }
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            inputStream.close();
            return classNode;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StringsTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StringsTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
