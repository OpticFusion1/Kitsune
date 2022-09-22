package optic_fusion1.kitsune.tool.impl.analyze.analyzer.vbs;

import java.io.File;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.vbs.Comment;
import optic_fusion1.kitsune.parser.vbs.ConstStatement;
import optic_fusion1.kitsune.parser.vbs.MsgBoxStatement;
import optic_fusion1.kitsune.parser.vbs.Parameter;
import optic_fusion1.kitsune.parser.vbs.SetStatement;
import optic_fusion1.kitsune.parser.vbs.Statement;
import optic_fusion1.kitsune.parser.vbs.VBSParser;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;

public class VBSAnalyzer extends Analyzer {

    @Override
    public void analyze(File input) {
        VBSParser parser = new VBSParser(input);
        parser.parse();
        for (Statement statement : parser.getParsedStatements()) {
            if (statement instanceof ConstStatement stmnt) {
                LOGGER.info("Const: " + stmnt.getName() + " Value: " + stmnt.getValue());
                continue;
            }
            if (statement instanceof MsgBoxStatement stmnt) {
                LOGGER.info("MsgBox: " + stmnt.getMessage());
                continue;
            }
            if (statement instanceof SetStatement stmnt) {
                LOGGER.info("Variable: " + stmnt.getVariableName() + " Value: " + stmnt.getVariableValue().getText());
                continue;
            }
            if (statement instanceof Comment comment) {
                LOGGER.info("Comment: " + comment.getText());
                continue;
            }
        }
    }

}
