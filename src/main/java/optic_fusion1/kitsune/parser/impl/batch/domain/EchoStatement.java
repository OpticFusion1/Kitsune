package optic_fusion1.kitsune.parser.impl.batch.domain;

import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;

// TODO: Handle `@echo off`, `echo.`, and other variations to this
public class EchoStatement extends Statement {

    private String value;

    public EchoStatement(String value) {
        this.value = value;
    }
    
    public String value() {
        return value;
    }

}
