package optic_fusion1.kitsune.parser.vbs.domain;

import java.util.ArrayList;
import java.util.List;

public class VariableInit extends Statement {

    private DataType variableType;
    private List<String> variables = new ArrayList<>();

    public List<String> getVariables() {
        return variables;
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }

    public DataType getVariableType() {
        return variableType;
    }

    public void setVariableType(DataType variableType) {
        this.variableType = variableType;
    }

}
