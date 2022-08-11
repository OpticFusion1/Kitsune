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
package optic_fusion1.kitsune.tool.impl.analyze.analyzer;

import optic_fusion1.kitsune.tool.impl.analyze.analyzer.file.ManifestMFFileAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.StreamCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.SQLCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.URLCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.ZipEntryCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.ThreadCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.RuntimeCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.MethodCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.ClassCodeAnalyzer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.AWTCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.CodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.FileCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.FilesCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.JNativeHookAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.JavassistAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.code.SystemCodeAnalyzer;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.file.FileAnalyzer;
import static optic_fusion1.kitsune.util.I18n.tl;
import optic_fusion1.kitsune.util.Utils;
import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.ParameterNode;

public class JarAnalyzer {

    /*
    TODO: Add a way to create Analyzer jar modules. Analyzer modules are jar files that contain one or more Analyzers.
    These modules would be useful to easily add analyzers for specific things without the need to fork the program e.g. Android
     */
    private static final HashMap<String, CodeAnalyzer> CODE_ANALYZERS = new HashMap<>();
    private static final List<FileAnalyzer> FILE_ANALYZERS = new ArrayList<>();

    public JarAnalyzer() {
        registerCodeAnalyzers();
        registerFileAnalyzers();
    }

    private void registerCodeAnalyzers() {
        registerCodeAnalyzer("java/io/File", new FileCodeAnalyzer());
        registerCodeAnalyzer("java/lang/Thread", new ThreadCodeAnalyzer());
        registerCodeAnalyzer("java/util/zip/ZipEntry", new ZipEntryCodeAnalyzer());
        registerCodeAnalyzer("java/lang/Runtime", new RuntimeCodeAnalyzer());
        registerCodeAnalyzer("java/lang/reflect/Method", new MethodCodeAnalyzer());
        registerCodeAnalyzer("java/net/URL", new URLCodeAnalyzer());
        registerCodeAnalyzer("java/io/BufferedInputStream", new StreamCodeAnalyzer("BufferedInputStream"));
        registerCodeAnalyzer("java/io/FileInputStream", new StreamCodeAnalyzer("FileInputStream"));
        registerCodeAnalyzer("java/io/FileOutputStream", new StreamCodeAnalyzer("FileOutputStream"));
        registerCodeAnalyzer("java/io/ObjectOutputStream", new StreamCodeAnalyzer("ObjectOutputStream"));
        registerCodeAnalyzer("java/sql/Connection", new SQLCodeAnalyzer("Connection"));
        registerCodeAnalyzer("java/sql/DriverManager", new SQLCodeAnalyzer("DriverManager"));
        registerCodeAnalyzer("java/lang/Class", new ClassCodeAnalyzer());
        registerCodeAnalyzer("java/awt/Robot", new AWTCodeAnalyzer());
        registerCodeAnalyzer("java/awt/Toolkit", new AWTCodeAnalyzer());
        registerCodeAnalyzer("java/lang/System", new SystemCodeAnalyzer());
        registerCodeAnalyzer("com/github/kwhat/jnativehook/keyboard/NativeKeyEvent", new JNativeHookAnalyzer());
        registerCodeAnalyzer("com/github/kwhat/jnativehook/GlobalScreen", new JNativeHookAnalyzer());
        registerCodeAnalyzer("java/nio/file/Files", new FilesCodeAnalyzer());
        registerCodeAnalyzer("javassist/CtMethod", new JavassistAnalyzer());
    }

    private void registerCodeAnalyzer(String methodInsnNodeOwner, CodeAnalyzer analyzer) {
        CODE_ANALYZERS.putIfAbsent(methodInsnNodeOwner, analyzer);
    }

    private void registerFileAnalyzers() {
        registerFileAnalyzer(new ManifestMFFileAnalyzer());
    }

    private void registerFileAnalyzer(FileAnalyzer analyzer) {
        FILE_ANALYZERS.add(analyzer);
    }

    public void analyze(File file) {
        LOGGER.info(tl("processing", file.toPath()));
        for (FileAnalyzer analyzer : FILE_ANALYZERS) {
            analyzer.analyze(file);
        }
        LOGGER.info(tl("ja_gathering_class_nodes"));
        List<ClassNode> classNodes = getClassNodesFromFile(file);
        if (classNodes.isEmpty()) {
            LOGGER.warn(tl("ja_class_nodes_not_found", file.toPath()));
            return;
        }
        LOGGER.info(tl("ja_processing_class_nodes"));
        classNodes.forEach(this::processClassNode);
    }

    private void processClassNode(ClassNode classNode) {
        // TODO: Make it so it carves out the entire class first and then process that

        // Logs Fields
        for (FieldNode node : classNode.fields) {
            log(classNode, node);
        }

        // Processes Methods
        for (MethodNode methodNode : classNode.methods) {

            // Logs Parameters
            if (methodNode.parameters != null) {
                for (ParameterNode parameter : methodNode.parameters) {
                    log(classNode, methodNode, parameter);
                }

            }

            // Analyzes each instruction
            for (AbstractInsnNode instruction : methodNode.instructions) {
                if (instruction instanceof MethodInsnNode method) {
                    String owner = method.owner;
                    CodeAnalyzer analyzer = CODE_ANALYZERS.get(owner);
                    if (analyzer != null) {
                        analyzer.analyze(classNode, methodNode, method);
                    }
                }
            }
        }
    }

    private List<ClassNode> getClassNodesFromFile(File file) {
        List<ClassNode> classNodes = new ArrayList<>();
        if (file.getName().endsWith(".class")) {
            ClassNode node = Utils.getClassNodeFromClassFile(file);
            classNodes.add(node);
            return classNodes;
        }
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
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
                classNodes.add(classNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNodes;
    }

}
