package optic_fusion1.kitsune.parser.vbs;

import java.util.ArrayList;
import java.util.List;

public class LoopStatement extends Statement implements IContainer {

    private List<IContainer> childContainers = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }

    @Override
    public List<IContainer> getChildContainers() {
        return childContainers;
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public List<Statement> filterStatements(StatementFilter filter) {

        return Utils.filterStatements(statements, filter);
    }
}
