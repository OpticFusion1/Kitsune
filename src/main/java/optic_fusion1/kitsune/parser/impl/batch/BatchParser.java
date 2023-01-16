package optic_fusion1.kitsune.parser.impl.batch;

import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.Parser;
import optic_fusion1.kitsune.parser.impl.batch.domain.Constants;
import optic_fusion1.kitsune.parser.impl.batch.domain.EchoStatement;
import optic_fusion1.kitsune.parser.impl.batch.domain.SetStatement;
import optic_fusion1.kitsune.parser.impl.batch.domain.StartStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Comment;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.impl.vbs.interfaces.IContainer;

public class BatchParser extends Parser {

    @Override
    public void identifyAndConvert(int index, String line) {
        String lineTrimmed = line.trim();
        if (lineTrimmed.toLowerCase().startsWith(Constants.COMMENT_IDENTIFIER)) {
            Comment comment = StatementFactory.buildComments(index, line);
            IContainer parent = getLastContainerFromStack();
            comment.setParent(parent);
            parent.getStatements().add(comment);
            parsedFile().getParsedStatements().add(comment);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.ECHO_IDENTIFIER)) {
            EchoStatement stmnt = StatementFactory.buildEchoStatement(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            stmnt.setParent(parent);
            parent.getStatements().add(stmnt);
            parsedFile().getParsedStatements().add(stmnt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.SET_IDENTIFIER)) {
            SetStatement stmnt = StatementFactory.buildSetStatement(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            stmnt.setParent(parent);
            parent.getStatements().add(stmnt);
            parsedFile().getParsedStatements().add(stmnt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.START_IDENTIFIER)) {
            StartStatement stmnt = StatementFactory.buildStartStatement(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            stmnt.setParent(parent);
            parent.getStatements().add(stmnt);
            parsedFile().getParsedStatements().add(stmnt);
        } else {
            if (DEBUG) {
                LOGGER.warn("The following line does not get parsed: " + lineTrimmed);
            }
            Statement stmnt = StatementFactory.buildGenericStatements(index, line);
            IContainer parent = getLastContainerFromStack();
            stmnt.setParent(parent);
            parent.getStatements().add(stmnt);
            parsedFile().getParsedStatements().add(stmnt);
        }
    }

}
