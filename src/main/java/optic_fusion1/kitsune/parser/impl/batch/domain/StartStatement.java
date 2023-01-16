package optic_fusion1.kitsune.parser.impl.batch.domain;

import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;

// TODO: implement arg support e.g. /MIN
public class StartStatement extends Statement {

    private String file;

    public StartStatement(String file) {
        this.file = file;
    }
    
    public String getFile() {
        return file;
    }

}
