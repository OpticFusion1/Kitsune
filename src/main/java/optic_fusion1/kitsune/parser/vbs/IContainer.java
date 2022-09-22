package optic_fusion1.kitsune.parser.vbs;

import java.util.List;

public interface IContainer {

    public List<IContainer> getChildContainers();

    public List<Statement> getStatements();

    public List<Statement> filterStatements(StatementFilter filter);

}
