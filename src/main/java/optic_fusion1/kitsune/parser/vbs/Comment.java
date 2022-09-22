package optic_fusion1.kitsune.parser.vbs;

public class Comment extends Statement {

    private String value;

    public Comment(String value) {
        this.value = value;
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }

    public String value() {
        return value;
    }
    
}
