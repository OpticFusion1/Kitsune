package optic_fusion1.kitsune.parser.vbs.domain;

public class Parameter {

    private String name;
    private DataType type;

    public Parameter(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
