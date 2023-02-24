package optic_fusion1.kitsune.parser.impl.batch.domain;

import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;

public class SetStatement extends Statement {

    private String name;
    private String value;

    public SetStatement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
