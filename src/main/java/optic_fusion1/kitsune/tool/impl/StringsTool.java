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
package optic_fusion1.kitsune.tool.impl;

import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.Utils.checkFileExists;
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
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import org.apache.commons.codec.digest.DigestUtils;
import org.objectweb.asm.tree.MethodInsnNode;

public class StringsTool extends Tool {

    private boolean normalize;
    private boolean removeDuplicateStrings;
    // TODO: Add support for different hashes
    private boolean showSha1Hash;

    public StringsTool() {
        super("strings", "Prints every string in a .jar or .class file. Usage: printStrings <all <directory_path>|file_path> [--normalize] [--removeDuplicates]");
    }

    @Override
    public void run(List<String> args) {
        // TODO: Add better arg handling
        if (args.isEmpty()) {
            LOGGER.info("You did not enter enough arguments. Usage: printStrings <all <directory_path>|file_path> [--normalize] [--removeDuplicates]");
        }
        if (args.contains("--normalize")) {
            args.remove("--normalize");
            normalize = true;
        }
        if (args.contains("--removeDuplicates")) {
            args.remove("--removeDuplicates");
            removeDuplicateStrings = true;
        }
        if (args.contains("--showSHA1Hash")) {
            args.remove("--showSHA1Hash");
            showSha1Hash = true;
        }
        if (!args.get(0).equalsIgnoreCase("all")) {
            File input = new File(args.get(0));
            if (!checkFileExists(input)) {
                LOGGER.info(input.toPath() + " does not exist");
                return;
            }
            if (!input.getName().endsWith(".jar") && !input.getName().endsWith(".class")) {
                LOGGER.info(input.toPath() + " is not a jar file");
                return;
            }
            if (input.isDirectory()) {
                LOGGER.info(input.toPath() + " is a directory");
                return;
            }
            processFile(input);
            return;
        }
        if (args.size() == 1) {
            LOGGER.info("You did not enter enough arguments. Usage: printStrings all <directory_path>");
            return;
        }
        File input = new File(args.get(1));
        if (!checkFileExists(input)) {
            LOGGER.info(input.toPath() + " does not exist");
            return;
        }
        if (!input.isDirectory()) {
            LOGGER.info(input.toPath() + " is not a directory");
            return;
        }
        for (File file : input.listFiles()) {
            LOGGER.info("\n\n\n\n");
            LOGGER.info("Processing " + file.getName() + ":");
            try {
                processFile(file);
            } catch (Exception e) {
                continue;
            }
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
            LOGGER.info(inputFile.toPath() + " is not a jar file");
            return;
        }
        try (ZipFile zipFile = new ZipFile(inputFile)) {
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
                ClassReader classReader;
                try {
                    classReader = new ClassReader(inputStream);
                } catch (Exception e) {
                    continue;
                }
                ClassNode classNode = new ClassNode();
                classReader.accept(classNode, 0);
                printStrings(classNode);
            }
        } catch (ZipException e) {
            LOGGER.exception(e);
        } catch (IOException e) {
            LOGGER.exception(e);
        }
    }

    private void printStrings(ClassNode classNode) {
        if (classNode.methods.isEmpty()) {
            LOGGER.info(classNode.name + " has no methods. Skipping");
            return;
        }
        if (classNode.fields.isEmpty()) {
            LOGGER.info(classNode.name + " has no fields. Skipping");
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
        LOGGER.info(string);
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

            String ldcString = ldcInsnNode.cst.toString();
            AbstractInsnNode minus1 = instruction.getPrevious();
            // TODO: Move Base64 to a general deobfuscation tool
            // TODO: Properly support Base64
            if (normalize) {
                ldcString = ldcString.toLowerCase().trim();
            }
            String decodedString = "";
            if (minus1 instanceof MethodInsnNode methodInsnNode && methodInsnNode.owner.equals("java/util/Base64")) {
                decodedString = new String(Base64.getDecoder().decode(ldcInsnNode.cst.toString()));
            }
            // TODO: Move Base64 to a general deobfuscation tool
            String finalString = classNode.name + "#" + methodNode.name + ": " + ldcString + (decodedString.isBlank() ? "" : " Decoded: " + decodedString);
            String sha1Hash = "";
            if (showSha1Hash) {
                sha1Hash = DigestUtils.sha1Hex(ldcInsnNode.cst.toString());
            }
            if (removeDuplicateStrings) {
                if (strings.contains(finalString)) {
                    continue;
                }
                LOGGER.info(finalString + (showSha1Hash ? ": " + sha1Hash : ""));
                strings.add(finalString);
                continue;
            }
            LOGGER.info(finalString + (showSha1Hash ? ": " + sha1Hash : ""));
        }
    }

}
