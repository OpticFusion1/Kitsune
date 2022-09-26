package optic_fusion1.kitsune.parser.vbs.domain;

public class ConstStatement extends Statement {

    private String name;
    private String value;

    public ConstStatement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return getClass();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
