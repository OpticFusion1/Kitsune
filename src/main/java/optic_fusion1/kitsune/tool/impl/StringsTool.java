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
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import static optic_fusion1.kitsune.util.I18n.tl;
import optic_fusion1.kitsune.util.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.objectweb.asm.tree.MethodInsnNode;

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
                LOGGER.info(tl("file_invalid_extension", input.toPath()));
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
            LOGGER.info(tl("file_invalid_extension", inputFile.toPath()));
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
            LOGGER.info(tl("st_class_no_methods", classNode.name));
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

    private static final Pattern PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");

    private boolean isPossibleBase64(String string) {
        if (string.isBlank() || string.getBytes().length < 2) {
            return false;
        }
        return PATTERN.matcher(string).find();
    }

    private String deobfBase64(String string) {
        String deobf = string;
        while (isPossibleBase64(deobf)) {
            deobf = new String(Base64.getDecoder().decode(deobf));
        }
        return deobf;
    }

    // TODO: Handle new String(new byte[] {})
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
            if (normalize) {
                ldcString = Utils.normalize(ldcString);
            }
            String decodedString = "";
            // TODO: Move Base64 to a general deobfuscation tool
            // TODO: Properly support Base64
            if (minus1 instanceof MethodInsnNode methodInsnNode && methodInsnNode.owner.equals("java/util/Base64")) {
                decodedString = deobfBase64(ldcString);
            } else if (isPossibleBase64(ldcString)) {
                decodedString = deobfBase64(ldcString);
            }
            // TODO: Come up with a good way to make this translatable
            String finalString = classNode.name + "#" + methodNode.name + ": " + ldcString + (decodedString.isBlank() ? "" : " Decoded: " + decodedString);
            String sha1Hash = "";
            if (showSha1Hash) {
                sha1Hash = DigestUtils.sha1Hex(ldcString);
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
