package optic_fusion1.kitsune.parser.vbs.interfaces;

import optic_fusion1.kitsune.parser.vbs.domain.Statement;
import java.util.List;
import optic_fusion1.kitsune.parser.vbs.StatementFilter;

public interface IContainer {

    public List<IContainer> getChildContainers();

    public List<Statement> getStatements();

    public List<Statement> filterStatements(StatementFilter filter);

}
