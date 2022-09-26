package optic_fusion1.kitsune.parser.vbs.domain;

import java.util.ArrayList;
import java.util.List;
import optic_fusion1.kitsune.parser.vbs.interfaces.IContainer;
import optic_fusion1.kitsune.parser.vbs.StatementFilter;
import optic_fusion1.kitsune.parser.vbs.util.Utils;

public class Function extends Statement implements IContainer {

    private String name;
    private List<IContainer> childContainers = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private List<Parameter> parameters = new ArrayList<>();

    public Function(String name) {
        this.name = name;
    }

    @Override
    public List<IContainer> getChildContainers() {
        return childContainers;
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public List<Statement> filterStatements(StatementFilter filter) {
        return Utils.filterStatements(statements, filter);
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }

}
