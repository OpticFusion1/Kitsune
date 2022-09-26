package optic_fusion1.kitsune.parser.vbs.domain;

public class SetStatement extends Statement {

    private String variableName;
    private Statement variableValue;

    public SetStatement(String variableName, Statement variableValue) {
        this.variableName = variableName;
        this.variableValue = variableValue;
    }

    @Override
    public Class<?> getType() {
        return getClass();
    }

    public String getVariableName() {
        return variableName;
    }

    public Statement getVariableValue() {
        return variableValue;
    }

}
