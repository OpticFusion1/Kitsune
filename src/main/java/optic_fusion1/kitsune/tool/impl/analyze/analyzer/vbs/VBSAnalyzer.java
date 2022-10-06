package optic_fusion1.kitsune.tool.impl.analyze.analyzer.vbs;

import java.io.File;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.vbs.VBSFile;
import optic_fusion1.kitsune.parser.vbs.VBSParser;
import optic_fusion1.kitsune.parser.vbs.domain.Comment;
import optic_fusion1.kitsune.parser.vbs.domain.ConstStatement;
import optic_fusion1.kitsune.parser.vbs.domain.MsgBoxStatement;
import optic_fusion1.kitsune.parser.vbs.domain.SetStatement;
import optic_fusion1.kitsune.parser.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.vbs.domain.VariableInit;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;

public class VBSAnalyzer extends Analyzer {

    @Override
    public void analyze(File input) {
        VBSParser parser = new VBSParser();
        parser.parse(new VBSFile(input));
        for (ConstStatement stmnt : parser.getVBSFile().getConstantPool().getStatements()) {
            LOGGER.info("Const: " + stmnt.getName().trim() + " Value: " + stmnt.getValue());
        }

        for (Statement statement : parser.getVBSFile().getParsedStatements()) {
            if (statement instanceof MsgBoxStatement stmnt) {
                LOGGER.info("MsgBox: " + stmnt.getMessage());
                continue;
            }
            if (statement instanceof SetStatement stmnt) {
                LOGGER.info("Variable: " + stmnt.getVariableName() + " Value: " + stmnt.getVariableValue().getText());
                continue;
            }
            if (statement instanceof Comment comment) {
                LOGGER.info("Comment: " + comment.value());
            }
            if (statement instanceof VariableInit stmnt) {
                LOGGER.info("Name: " + stmnt.getName() + " Variable Type: " + stmnt.getVariableType());
                for (String variable : stmnt.getVariables()) {
                    LOGGER.info("Variable: " + variable);
                }
            }
        }
    }

}
