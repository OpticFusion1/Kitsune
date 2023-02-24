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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.I18n.tl;
import optic_fusion1.kitsune.util.Utils;
import static optic_fusion1.kitsune.util.Utils.checkFileExists;
import org.apache.commons.codec.digest.DigestUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

// TODO: Cleanup class if possible
public class StringsTool extends Tool {

    private boolean normalize;
    private boolean removeDuplicateStrings;
    // TODO: Add support for different hashes
    private boolean showSha1Hash;

    public StringsTool() {
        super("strings", tl("st_desc") + " " + tl("st_usage"));
    }

    @Override
    public void run(List<String> args) {
        // TODO: Add better arg handling
        if (args.isEmpty()) {
            LOGGER.info(tl("not_enough_args") + " " + tl("st_usage"));
            return;
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
                LOGGER.info(tl("file_does_not_exist", input.toPath()));
                return;
            }
            if (!input.getName().endsWith(".jar") && !input.getName().endsWith(".class")) {
                LOGGER.info(tl("invalid_extension", input.toPath()));
                return;
            }
            if (input.isDirectory()) {
                LOGGER.info(tl("file_is_directory", input.toPath()));
                return;
            }
            processFile(input);
            return;
        }
        if (args.size() == 1) {
            LOGGER.info(tl("not_enough_args") + " " + tl("st_usage"));
            return;
        }
        File input = new File(args.get(1));
        if (!checkFileExists(input)) {
            LOGGER.info(tl("file_does_not_exist", input.toPath()));
            return;
        }
        if (!input.isDirectory()) {
            LOGGER.info(tl("file_is_not_directory", input.toPath()));
            return;
        }
        for (File file : input.listFiles()) {
            LOGGER.info("\n\n\n\n");
            LOGGER.info(tl("processing", file.getName()));
            try {
                processFile(file);
            } catch (Exception e) {
                LOGGER.exception(e);
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
        if (!inputFile.getName().endsWith(".jar") && !inputFile.getName().endsWith(".class")) {
            LOGGER.info(tl("invalid_extension", inputFile.toPath()));
            return;
        }
        if (inputFile.getName().endsWith(".jar")) {
            handleJarFile(inputFile);
            return;
        }
        handleClassFile(inputFile);
    }

    private void handleClassFile(File inputFile) {
        ClassNode node = Utils.getClassNodeFromClassFile(inputFile);
        if (node == null) {
            LOGGER.warn("Couldn't get a ClassNode from " + inputFile);
            return;
        }
        printStrings(node);
    }

    private void handleJarFile(File inputFile) {
        try (ZipFile zipFile = new ZipFile(inputFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }
                InputStream inputStream = zipFile.getInputStream(entry);
                if (inputStream == null) {
                    LOGGER.warn("Couldn't get entry " + entry.getName() + " for file " + inputFile);
                    continue;
                }
                ClassReader classReader;
                try {
                    classReader = new ClassReader(inputStream);
                } catch (IOException e) {
                    LOGGER.warn("Couldn't read InputStream for file " + inputFile);
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
        if (!classNode.fields.isEmpty()) {
            for (FieldNode field : classNode.fields) {
                handleField(classNode, field);
            }
        } else {
            LOGGER.info(tl("st_class_no_fields", classNode.name));
        }

        if (!classNode.methods.isEmpty()) {
            for (MethodNode method : classNode.methods) {
                handleMethod(classNode, method);
            }
        } else {
            LOGGER.info(tl("st_class_no_methods", classNode.name));
        }
    }

    // TODO: Handle new String(new byte[] {})
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

    // TODO: Handle new String(new byte[] {})
    private void handleMethod(ClassNode classNode, MethodNode methodNode) {
        List<String> strings = new ArrayList<>();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            String string = "";
            if (instruction instanceof LdcInsnNode ldcInsnNode && ldcInsnNode.cst instanceof String) {
                string = (String) ldcInsnNode.cst;
            } else {
                // TODO: Skip over the instructions found via this method so the single chars don't get printed
                if (instruction.getOpcode() == Opcodes.BIPUSH) {
                    IntInsnNode intInsnNode = (IntInsnNode) instruction;
                    string = readStringArray(intInsnNode);
                    if (string.isBlank()) {
                        continue;
                    }
                }
            }
            if (string.isBlank()) {
                continue;
            }
            // TODO: Add decoding Base64 support. 
            // There was previously support for it however certain strings caused a false-positive, which broke the SHA1 hash
            String normalizedString = normalize ? Utils.normalize(string) : "";
            String sha1Hash = showSha1Hash ? DigestUtils.sha1Hex(normalizedString.isEmpty() ? string : normalizedString) : "";
            StringBuilder builder = new StringBuilder();
            builder.append(classNode.name).append("#");
            builder.append(methodNode.name).append(": ");
            builder.append("Original String: ").append(string);
            if (!normalizedString.isBlank()) {
                builder.append(" Normalized: ").append(normalizedString);
            }
            if (!sha1Hash.isBlank()) {
                builder.append(" SHA1 Hash: ").append(sha1Hash);
            }
            String finalString = builder.toString();
            if (removeDuplicateStrings) {
                if (strings.contains(finalString)) {
                    continue;
                }
                LOGGER.info(finalString);
                strings.add(finalString);
                continue;
            }
            LOGGER.info(finalString);
        }
    }

    private String readStringArray(IntInsnNode intInsnNode) {
        if (intInsnNode.getNext().getOpcode() != Opcodes.ANEWARRAY) {
            return "";
        }
        int numOfElements = intInsnNode.operand;
        return parseStringElement(intInsnNode, 0, numOfElements, "");
    }

    private String parseStringElement(AbstractInsnNode insnNode, int timesRan, int maxElements, String string) {
        if (timesRan == maxElements) {
            return string;
        }
        LdcInsnNode node = (LdcInsnNode) insnNode.getNext().getNext().getNext().getNext();
        String s = (String) node.cst;
        string += s;
        timesRan++;
        return parseStringElement(node, timesRan, maxElements, string);
    }

}
