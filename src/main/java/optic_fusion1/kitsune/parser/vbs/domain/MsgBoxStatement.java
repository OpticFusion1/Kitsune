package optic_fusion1.kitsune.parser.vbs.domain;

public class MsgBoxStatement extends Statement {

    private String message;

    public MsgBoxStatement(String message) {
        this.message = message;
    }

    @Override
    public Class<?> getType() {
        return getClass();
    }
    
    public String getMessage() {
        return message;
    }

}
