package optic_fusion1.kitsune.tool.impl.analyze.analyzer.code;

import static optic_fusion1.kitsune.util.Utils.log;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class AWTCodeAnalyzer extends CodeAnalyzer {

    @Override
    public void analyze(ClassNode classNode, MethodNode methodNode, MethodInsnNode methodInsnNode) {
        if (isMethodInsnNodeCorrect(methodInsnNode, "<init>", "()V")) {
            log(classNode, methodNode, methodInsnNode, "Robot Initialized");
            return;
        }
        if (isMethodInsnNodeCorrect(methodInsnNode, "createScreenCapture", "(Ljava/awt/Rectangle;)Ljava/awt/image/BufferedImage;")) {
            log(classNode, methodNode, methodInsnNode, "Screen Capture Taken");
        }
        /*
        ()V <init> java/awt/Robot
()Ljava/awt/Toolkit; getDefaultToolkit java/awt/Toolkit
()Ljava/awt/Dimension; getScreenSize java/awt/Toolkit
(Ljava/awt/Rectangle;)Ljava/awt/image/BufferedImage; createScreenCapture java/awt/Robot
         */
//        System.out.println(methodInsnNode.desc + " " + methodInsnNode.name + " " + methodInsnNode.owner);
//        if (methodNode.desc.equals("{}V")) {
//            log(classNode, methodNode, methodInsnNode, "Robot Initialized");
//        }
        /*
               0: new             Ljava/awt/Robot;
               3: dup            
               4: invokespecial   java/awt/Robot.<init>:()V
               7: astore_1         screenCapture
                  linenumber      45
               8: aload_1          screenCapture 
               9: new             Ljava/awt/Rectangle;
              12: dup            
              13: invokestatic    java/awt/Toolkit.getDefaultToolkit:()Ljava/awt/Toolkit;
              16: invokevirtual   java/awt/Toolkit.getScreenSize:()Ljava/awt/Dimension;
              19: invokespecial   java/awt/Rectangle.<init>:(Ljava/awt/Dimension;)V
              22: invokevirtual   java/awt/Robot.createScreenCapture:(Ljava/awt/Rectangle;)Ljava/awt/image/BufferedImage;        
         */
    }

}
