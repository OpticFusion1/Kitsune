package optic_fusion1.jre.tool.impl.analyze;

import optic_fusion1.jre.tool.Tool;
import optic_fusion1.jre.tool.impl.analyze.analyzer.JavaAnalyzer;
import static optic_fusion1.jre.util.Utils.checkFileExists;
import java.io.File;
import java.util.List;

public class AnalyzeTool extends Tool {

    private static final JavaAnalyzer JAVA_ANALYZER = new JavaAnalyzer();

    public AnalyzeTool() {
        super("analyze", "Analyzes a jar or class and provides a detailed report");
    }

    @Override
    public void run(List<String> args) {
        if (args.isEmpty()) {
            System.out.println("You did not enter enough arguments. Usage: analyze <file>");
            return;
        }
        File input = new File(args.get(0));
        if (!checkFileExists(input)) {
            System.out.println(input.toPath() + " does not exist");
            return;
        }
        if (!input.getName().endsWith(".jar") && !input.getName().endsWith(".class") || input.isDirectory()) {
            System.out.println("You can only analyze .jar and .class files");
            return;
        }
        JAVA_ANALYZER.analyze(input);
    }
}
/*

    private void processReflectionStuff(ClassNode classNode, MethodNode methodNode, MethodInsnNode method) {
        if (method.name.equals("invoke") && method.desc.equals("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;")) {
            log(classNode, method, "Method#invoke");
        }
    }

    private void processInternetStuff(ClassNode classNode, MethodNode methodNode, MethodInsnNode method) {
        if (method.owner.equals("java/net/URL") && method.desc.equals("(Ljava/lang/String;)V")) {
            log(classNode, method, "URL");
            /*
            AbstractInsnNode minus1 = method.getPrevious();
            if (minus1 instanceof LdcInsnNode ldc) {
                if (ldc.cst instanceof String) {
                    log(classNode, )
                }
            }
            */
        //}
   // }

    //private void printOutput() {
//
 //   }
//}