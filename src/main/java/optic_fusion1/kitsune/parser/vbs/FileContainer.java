package optic_fusion1.kitsune.parser.vbs;

import java.util.ArrayList;
import java.util.List;

public class FileContainer implements IContainer {

    private String name;
    private List<IContainer> childContainers = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();

    public FileContainer(String name) {
        setName(name);
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

    @Override
    public List<Statement> filterStatements(StatementFilter filter) {
        return Utils.filterStatements(statements, filter);
    }

}
