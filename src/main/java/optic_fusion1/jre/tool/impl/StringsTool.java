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
package optic_fusion1.jre.tool.impl;

import optic_fusion1.jre.tool.Tool;
import static optic_fusion1.jre.util.Utils.checkFileExists;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class StringsTool extends Tool {

    private boolean normalize;
    private boolean removeDuplicateStrings;

    public StringsTool() {
        super("strings", "Prints every string in a .jar or .class file. Usage: printStrings <all <directory_path>|file_path> [--normalize] [--removeDuplicates]");
    }

    @Override
    public void run(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("You did not enter enough arguments. Usage: printStrings <all <directory_path>|file_path> [--normalize] [--removeDuplicates]");
        }
        if (args.contains("--normalize")) {
            args.remove("--normalize");
            normalize = true;
        }
        if (args.contains("--removeDuplicates")) {
            args.remove("--removeDuplicates");
            removeDuplicateStrings = true;
        }
        if (!args.get(0).equalsIgnoreCase("all")) {
            File input = new File(args.get(0));
            if (!checkFileExists(input)) {
                System.out.println(input.toPath() + " does not exist");
                return;
            }
            if (!input.getName().endsWith(".jar") && !input.getName().endsWith(".class")) {
                System.out.println(input.toPath() + " is not a jar file");
                return;
            }
            if (input.isDirectory()) {
                System.out.println(input.toPath() + " is a directory");
                return;
            }
            processFile(input);
            return;
        }
        if (args.size() == 1) {
            System.out.println("You did not enter enough arguments. Usage: printStrings all <directory_path>");
            return;
        }
        File input = new File(args.get(1));
        if (!checkFileExists(input)) {
            System.out.println(input.toPath() + " does not exist");
            return;
        }
        if (!input.isDirectory()) {
            System.out.println(input.toPath() + " is not a directory");
            return;
        }
        for (File file : input.listFiles()) {
            processFile(file);
        }
    }

    private void processFile(File inputFile) {
        if (inputFile.isDirectory()) {
            for (File file : inputFile.listFiles()) {
                processFile(file);
            }
            return;
        }
        if (!inputFile.getName().endsWith(".jar")) {
            System.out.println(inputFile.toPath() + " is not a jar file");
            return;
        }
        try ( ZipFile zipFile = new ZipFile(inputFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }
                InputStream inputStream = zipFile.getInputStream(entry);
                if (inputStream == null) {
                    continue;
                }
                ClassReader classReader = new ClassReader(inputStream);
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);
                printStrings(classNode);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printStrings(ClassNode classNode) {
        if (classNode.methods.isEmpty()) {
            System.out.println(classNode.name + " has no methods. Skipping");
            return;
        }
        if (classNode.fields.isEmpty()) {
            System.out.println(classNode.name + " has no fields. Skipping");
        }
        for (MethodNode method : classNode.methods) {
            handleMethod(classNode, method);
        }
        for (FieldNode field : classNode.fields) {
            handleField(classNode, field);
        }
    }

    private void handleField(ClassNode classNode, FieldNode fieldNode) {
        Object value = fieldNode.value;
        if (!(value instanceof String)) {
            return;
        }
        String string = (String) fieldNode.value;
        string = classNode.name + "#" + fieldNode.name + ": " + string;
        if (normalize) {
            string = string.toLowerCase().trim();
        }
        System.out.println(string);
    }

    private void handleMethod(ClassNode classNode, MethodNode methodNode) {
        List<String> strings = new ArrayList<>();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            if (!(instruction instanceof LdcInsnNode ldcInsnNode)) {
                continue;
            }
            if (!(ldcInsnNode.cst instanceof String)) {
                continue;
            }
            String string = ldcInsnNode.cst.toString();
            if (normalize) {
                string = string.toLowerCase().trim();
            }
            string = classNode.name + "#" + methodNode.name + ": " + string;
            if (removeDuplicateStrings) {
                if (strings.contains(string)) {
                    continue;
                }
                System.out.println(string);
                strings.add(string);
                continue;
            }
            System.out.println(string);
        }
    }

}
