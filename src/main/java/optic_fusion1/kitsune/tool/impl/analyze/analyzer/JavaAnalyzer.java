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

public class JavaAnalyzer {

    private static final HashMap<String, Analyzer> ANALYZERS = new HashMap<>();

    public JavaAnalyzer() {
        registerAnalyzers();
    }

    protected void registerAnalyzers() {
        registerAnalyzer("java/io/File", new FileAnalyzer());
        registerAnalyzer("java/lang/Thread", new ThreadAnalyzer());
        registerAnalyzer("java/util/zip/ZipEntry", new ZipEntryAnalyzer());
        registerAnalyzer("java/lang/Runtime", new RuntimeAnalyzer());
        registerAnalyzer("java/lang/reflect/Method", new MethodAnalyzer());
        registerAnalyzer("java/net/URL", new URLAnalyzer());
        registerAnalyzer("java/io/BufferedInputStream", new StreamAnalyzer("BufferedInputStream"));
        registerAnalyzer("java/io/FileInputStream", new StreamAnalyzer("FileInputStream"));
        registerAnalyzer("java/io/FileOutputStream", new StreamAnalyzer("FileOutputStream"));
        registerAnalyzer("java/io/ObjectOutputStream", new StreamAnalyzer("ObjectOutputStream"));
        registerAnalyzer("java/sql/Connection", new SQLAnalyzer("Connection")); 
        registerAnalyzer("java/sql/DriverManager", new SQLAnalyzer("DriverManager"));
        registerAnalyzer("java/lang/Class", new ClassAnalyzer());
    }

    protected void registerAnalyzer(String methodInsnNodeOwner, Analyzer analyzer) {
        ANALYZERS.putIfAbsent(methodInsnNodeOwner, analyzer);
    }

    public void analyze(File file) {
        System.out.println("Gathering all class nodes in " + file.toPath());
        List<ClassNode> classNodes = getClassNodesFromFile(file);
        if (classNodes.isEmpty()) {
            System.out.println(file.toPath() + " does not contain any class nodes");
            return;
        }
        System.out.println("Processing class nodes");
        classNodes.forEach(this::processClassNode);
    }

    private void processClassNode(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            for (AbstractInsnNode instruction : methodNode.instructions) {
                if (instruction instanceof MethodInsnNode method) {
                    String owner = method.owner;
                    Analyzer analyzer = ANALYZERS.get(owner);
                    if (analyzer != null) {
                        analyzer.analyze(classNode, methodNode, method);
                    }
                }
            }
        }
    }

    private List<ClassNode> getClassNodesFromFile(File file) {
        List<ClassNode> classNodes = new ArrayList<>();
        try ( ZipFile zipFile = new ZipFile(file)) {
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
