package optic_fusion1.kitsune.parser.vbs.domain;

public enum DataType {

    // TODO: Look to see if there's any other DataTypes that should be added to this
    STRING("String"),
    INT("Int"),
    VAR("var"),
    BY_VAL("ByVal"),
    BY_REF("ByRef"),
    SINGLE("Single"),
    EMPTY("");

    private final String name;

    private DataType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static DataType fromString(String text) {
        if (text != null) {
            for (DataType b : DataType.values()) {
                if (text.equalsIgnoreCase(b.name)) {
                    return b;
                }
            }
        }
        return VAR;
    }
}
