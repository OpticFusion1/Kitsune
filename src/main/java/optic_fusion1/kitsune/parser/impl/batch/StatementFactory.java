package optic_fusion1.kitsune.parser.impl.batch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optic_fusion1.kitsune.parser.impl.batch.domain.EchoStatement;
import optic_fusion1.kitsune.parser.impl.batch.domain.SetStatement;
import optic_fusion1.kitsune.parser.impl.batch.domain.StartStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Comment;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;

public final class StatementFactory {

    private StatementFactory() {
    }

    public static Statement buildGenericStatements(int index, String line) {
        Statement stmt = new Statement();
        stmt.setLineNumber(index);
        stmt.setText(line);
        return stmt;
    }

    private static final Pattern ECHO_PATTERN = Pattern.compile("echo (.*)");

    public static EchoStatement buildEchoStatement(int index, String lineTrimmed) {
        Matcher matcher = ECHO_PATTERN.matcher(lineTrimmed);
        matcher.find();
        String value = matcher.group(1);
        EchoStatement stmnt = new EchoStatement(value);
        return stmnt;
    }

    private static final Pattern COMMENT_PATTERN = Pattern.compile("::\\s*(.*)");

    public static Comment buildComments(int index, String lineTrimmed) {
        Matcher matcher = COMMENT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        String value = matcher.group(1);
        Comment comment = new Comment(value);
        return comment;
    }

    // SET username=%vso_username%
    private static final Pattern SET_PATTERN = Pattern.compile("set\\s+(\\w+)=(.+)");

    public static SetStatement buildSetStatement(int index, String lineTrimmed) {
        Matcher matcher = SET_PATTERN.matcher(lineTrimmed);
        matcher.find();
        String name = matcher.group(1);
        String value = matcher.group(2);
        SetStatement stmnt = new SetStatement(name, value);
        return stmnt;
    }

    private static final Pattern START_PATTERN = Pattern.compile("start\\s(.*)");

    public static final StartStatement buildStartStatement(int index, String lineTrimmed) {
        Matcher matcher = START_PATTERN.matcher(lineTrimmed);
        matcher.find();
        String file = matcher.group(1);
        StartStatement stmnt = new StartStatement(file);
        return stmnt;
    }

}
