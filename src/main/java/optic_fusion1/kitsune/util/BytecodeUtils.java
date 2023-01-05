package optic_fusion1.kitsune.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public final class BytecodeUtils implements Opcodes {

    private BytecodeUtils() {

    }

    public static boolean isFieldInsnNodeCorrect(FieldInsnNode fieldInsnNode, String owner, String name, String desc) {
        return fieldInsnNode.owner.equals(owner) && fieldInsnNode.name.equals(name) && fieldInsnNode.desc.equals(desc);
    }

    public static boolean isMethodInsnNodeCorrect(MethodInsnNode methodInsnNode, String owner, String name) {
        return methodInsnNode.owner.equals(owner) && methodInsnNode.name.equals(name);
    }

    public static boolean isMethodInsnNodeCorrect(MethodInsnNode methodInsnNode, String owner, String name, String desc) {
        return methodInsnNode.owner.equals(owner) && methodInsnNode.name.equals(name) && methodInsnNode.desc.equals(desc);
    }

    public static boolean isAbstractNodeInt(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof Integer;
    }

    public static boolean isAbstractNodeDouble(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof Double;
    }

    public static boolean isAbstractNodeLong(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof Long;
    }

    public static boolean isAbstractNodeFloat(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof Float;
    }

    public static boolean isAbstractNodeString(AbstractInsnNode node) {
        if (!(node instanceof LdcInsnNode ldcInsnNode)) {
            return false;
        }
        return ldcInsnNode.cst instanceof String;
    }

    public static boolean matches(AbstractInsnNode node, int value) {
        int opCode = node.getOpcode();
        if (opCode == ICONST_0 && value == 0
                || opCode == ICONST_1 && value == 1
                || opCode == ICONST_2 && value == 2
                || opCode == ICONST_3 && value == 3
                || opCode == ICONST_4 && value == 4
                || opCode == ICONST_5 && value == 5) {
            return true;
        }
        if (opCode == POP2) {
            return true;
        }
        if (opCode == BIPUSH || opCode == SIPUSH) {
            return ((IntInsnNode) node).operand == value;
        } else if (node.getOpcode() == LDC) {
            Object cst = ((LdcInsnNode) node).cst;
            return cst instanceof Integer && cst.equals(value);
        }
        return false;
    }

}
