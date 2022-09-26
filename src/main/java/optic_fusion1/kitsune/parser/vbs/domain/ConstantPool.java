package optic_fusion1.kitsune.parser.vbs.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ConstantPool {

    private static final HashMap<String, ConstStatement> STATEMENTS = new HashMap<>();

    public Collection<ConstStatement> getStatements() {
        return Collections.unmodifiableCollection(STATEMENTS.values());
    }

    public ConstStatement getStatement(String name) {
        return STATEMENTS.get(name);
    }

    public void addStatement(ConstStatement stmnt) {
        STATEMENTS.putIfAbsent(stmnt.getName(), stmnt);
    }

}
