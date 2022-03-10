package optic_fusion1.jre.tool.impl.analyze.analyzer;

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
