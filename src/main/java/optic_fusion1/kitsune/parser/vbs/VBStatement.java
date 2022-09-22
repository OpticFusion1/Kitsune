package optic_fusion1.kitsune.parser.vbs;

public class VBStatement extends Statement {

    private String text;

    private IContainer parent;

    private int lineNumber;

    @Override
    public Class<?> getType() {

        return this.getClass();
    }

    @Override
    public String getText() {

        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public IContainer getParentContainer() {
        return parent;
    }

    public void setParentContainer(IContainer parent) {
        this.parent = parent;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

}
