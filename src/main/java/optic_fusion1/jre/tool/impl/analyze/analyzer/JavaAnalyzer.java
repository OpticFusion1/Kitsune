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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class JavaAnalyzer {

    private static final FileAnalyzer FILE_ANALYZER = new FileAnalyzer();
    private static final ThreadAnalyzer THREAD_ANALYZER = new ThreadAnalyzer();
    private static final ZipEntryAnalyzer ZIP_ENTRY_ANALYZER = new ZipEntryAnalyzer();
    private static final RuntimeAnalyzer RUNTIME_ANALYZER = new RuntimeAnalyzer();
    private static final MethodAnalyzer METHOD_ANALYZER = new MethodAnalyzer();

    public void analyze(File file) {
        System.out.println("Gathering all class nodes in " + file.toPath());
        List<ClassNode> classNodes = getClassNodesFromFile(file);
        if (classNodes.isEmpty()) {
            System.out.println(file.toPath() + " does not contain any class nodes");
            return;
        }
        System.out.println("Processing class nodes");
        classNodes.forEach((node)->{
            processClassNode(node);
        });
    }

    private void processClassNode(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            for (AbstractInsnNode instruction : methodNode.instructions) {
                if (instruction instanceof MethodInsnNode method) {
                    String owner = method.owner;
                    if (owner.equals("java/io/File")) {
                        FILE_ANALYZER.analyze(classNode, methodNode, method);
                    }
                    if (owner.equals("java/lang/Thread")) {
                        THREAD_ANALYZER.analyze(classNode, methodNode, method);
                    }
                    if (owner.equals("java/util/zip/ZipEntry")) {
                        ZIP_ENTRY_ANALYZER.analyze(classNode, methodNode, method);
                    }
                    if (owner.equals("java/lang/Runtime")) {
                        RUNTIME_ANALYZER.analyze(classNode, methodNode, method);
                    }
                    if (owner.equals("java/lang/reflect/Method")) {
                        METHOD_ANALYZER.analyze(classNode, methodNode, method);
                    }
                }
            }
        }
    }

    private List<ClassNode> getClassNodesFromFile(File file) {
        List<ClassNode> classNodes = new ArrayList<>();
        try(ZipFile zipFile = new ZipFile(file)) {
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
                classNodes.add(classNode);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNodes;
    }
}
