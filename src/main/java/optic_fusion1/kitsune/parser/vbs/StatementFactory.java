package optic_fusion1.kitsune.parser.vbs;

import optic_fusion1.kitsune.parser.vbs.domain.Parameter;
import optic_fusion1.kitsune.parser.vbs.domain.VBStatement;
import optic_fusion1.kitsune.parser.vbs.domain.Comment;
import optic_fusion1.kitsune.parser.vbs.domain.LoopStatement;
import optic_fusion1.kitsune.parser.vbs.domain.ConstStatement;
import optic_fusion1.kitsune.parser.vbs.domain.ElseIfStatement;
import optic_fusion1.kitsune.parser.vbs.domain.VariableInit;
import optic_fusion1.kitsune.parser.vbs.domain.IfStatement;
import optic_fusion1.kitsune.parser.vbs.domain.ElseStatement;
import optic_fusion1.kitsune.parser.vbs.domain.MsgBoxStatement;
import optic_fusion1.kitsune.parser.vbs.domain.SetStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import optic_fusion1.kitsune.parser.vbs.domain.Constants;
import optic_fusion1.kitsune.parser.vbs.domain.DataType;
import optic_fusion1.kitsune.parser.vbs.domain.Function;

/*
TODO: Implement support for Sub functions
Sub SubName( param1, param2 ) || Sub SubName(param1,param2) // Or other variants
    // Statements
End Sub
 */
// TODO: Implement support for storing the values that variables get set to
// TODO: Implement support for parsing operators (=, +, -, /, *, %)
// TODO: Implement support for parsing things like Array(1,2) or Array ()
// TODO: Implement support for parsing function calls like reader.ReadFromFile WScript.Arguments(0) or WScript.Echo "Found barcode with type '" & reader.GetFoundBarcodeType(i) & "' and value '" & reader.GetFoundBarcodeValue(i) & "'"
// TODO: Implement support for parsing Classes
/*
TODO: Implement support for parsing Property

Public Property Get Connection()
    // Statements
End Property

Public Property Get Fields(strField)
    // Statements
End Property

Public Property Let AutoPercentEntry(blnAutoPercentEntry)
	AutoPercentEntry = p_Excel.AutoPercentEntry
End Property

Public Property Set AutomationSecurity(objMsoAutomationSecurity)
	Set p_Excel.AutomationSecurity = objMsoAutomationSecurity
End Property

*/
public class StatementFactory {


    /*
        TODO: Implement support for
        Function FUNCTIONNAME
            // Statements
        End Function
     */
    // TODO: Find a way to make a private static final Pattern variable for this classes's regex
    // TODO: Add support for getting if the function is Public, Private, or neither
    public static Function buildFunctionStatements(int index, String line) {
        Pattern pattern = Pattern.compile("( +.*?)\\(", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        Function func = new Function(matcher.group(1));
        func.setLineNumber(index);
        func.setText(line);
        pattern = Pattern.compile("\\((.*?)\\)", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(line);
        matcher.find();
        String paramstr = null;
        paramstr = matcher.group(1);
        if (paramstr != null) {
            String[] params = paramstr.split(",");
            for (String param : params) {
                param = param.trim();
                DataType paramType = DataType.VAR;
                String paramName = param;
                String[] pvPair = param.split("( +)");
                if (pvPair.length > 1) {
                    paramType = DataType.valueOf(pvPair[0].trim());
                    paramName = pvPair[1].trim();
                }
                Parameter p = new Parameter(paramName, paramType);
                func.getParameters().add(p);
            }
        }
        return func;
    }

    public static VariableInit buildVariableInitStatements(int index, String lineTrimmed) {
        VariableInit vinit = new VariableInit();
        vinit.setText(lineTrimmed);
        vinit.setLineNumber(index);
        Pattern pattern;
        Matcher matcher;
        if (lineTrimmed.contains(":") && lineTrimmed.contains("=")) {
            // TODO: Check to see if this REGEX Pattern can be improved
            pattern = Pattern.compile("Dim (.*) : (.*) = (.*)", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(lineTrimmed);
            matcher.find();
            vinit.setName(matcher.group(1));
            vinit.getVariables().add(matcher.group(3));
            return vinit;
        }
        // TODO: Make private static final Pattern for this
        if (lineTrimmed.contains("As")) {
            pattern = Pattern.compile("Dim(.+?)As", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(lineTrimmed);
        } else {
            pattern = Pattern.compile("Dim(.*)", Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(lineTrimmed);
        }
        matcher.find();
        String varstr = matcher.group(1);
        String[] variableNames = varstr.split(",");
        for (String var : variableNames) {
            vinit.getVariables().add(var.trim());
        }
        pattern = Pattern.compile("As(.+)", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(lineTrimmed);
        if (matcher.find()) {
            String vartype = matcher.group(1);
            vinit.setVariableType(DataType.fromString(vartype.trim()));
        } else {
            vinit.setVariableType(DataType.EMPTY);
        }
        return vinit;
    }

    public static VBStatement buildGenericStatements(int index, String line) {
        VBStatement stmt = new VBStatement();
        stmt.setLineNumber(index);
        stmt.setText(line);
        return stmt;
    }

    public static LoopStatement buildLoopStatements(int index, String lineTrimmed) {
        LoopStatement loop = new LoopStatement();
        loop.setText(lineTrimmed);
        loop.setLineNumber(index);
        String loopStartRegex = Constants.WHILE_IDENTIFIER + "|" + Constants.DO_WHILE_IDENTIFIER + "|" + Constants.FOR_IDENTIFIER;
        String condition = lineTrimmed.replaceFirst(loopStartRegex, "");
        loop.setCondition(condition);
        return loop;
    }

    private static final Pattern IF_STMNT_PATTERN = Pattern.compile("if(.+)then", Pattern.CASE_INSENSITIVE);

    public static IfStatement buildIFStatements(int index, String lineTrimmed) {
        Matcher matcher = IF_STMNT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        IfStatement ifstmt = new IfStatement();
        ifstmt.setText(lineTrimmed);
        ifstmt.setLineNumber(index);
        String condition = matcher.group(1);
        ifstmt.setCondition(condition);
        return ifstmt;
    }

    private static final Pattern SET_STMNT_PATTERN = Pattern.compile("Set (.+) ?= ?(.+)", Pattern.CASE_INSENSITIVE);

    public static SetStatement buildSetStatmenets(int index, String lineTrimmed) {
        Matcher matcher = SET_STMNT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        VBStatement stmt = new VBStatement();
        stmt.setText(matcher.group(2));
        SetStatement setStmt = new SetStatement(matcher.group(1).trim(), stmt);
        return setStmt;
    }

    private static final Pattern ELSE_IF_STMNT_PATTERN = Pattern.compile("elseif(.+)then", Pattern.CASE_INSENSITIVE);

    public static ElseIfStatement buildElseIFStatements(int index, String lineTrimmed) {
        Matcher matcher = ELSE_IF_STMNT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        ElseIfStatement ifstmt = new ElseIfStatement();
        ifstmt.setText(lineTrimmed);
        ifstmt.setLineNumber(index);
        String condition = matcher.group(1);
        ifstmt.setCondition(condition);
        return ifstmt;
    }

    public static ElseStatement buildElseStatements(int index, String lineTrimmed) {
        ElseStatement elsestmt = new ElseStatement();
        elsestmt.setText(lineTrimmed);
        elsestmt.setLineNumber(index);
        return elsestmt;
    }

    private static final Pattern MSG_BOX_STMNT_PATTERN = Pattern.compile("(msg|MsgBox) (.*)", Pattern.CASE_INSENSITIVE);

    public static MsgBoxStatement buildMsgBoxStatement(int index, String lineTrimmed) {
        Matcher matcher = MSG_BOX_STMNT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        MsgBoxStatement statement = new MsgBoxStatement(matcher.group(2));
        return statement;
    }

    private static final Pattern CONST_STMNT_PATTERN = Pattern.compile("Const (.*) = (.*)", Pattern.CASE_INSENSITIVE);

    public static ConstStatement buildConstStatements(int index, String lineTrimmed) {
        Matcher matcher = CONST_STMNT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        String name = matcher.group(1);
        String value = matcher.group(2);
        ConstStatement statement = new ConstStatement(name, value);
        return statement;
    }

    private static final Pattern COMMENT_PATTERN = Pattern.compile("'(.*)");

    public static Comment buildComments(int index, String lineTrimmed) {
        Matcher matcher = COMMENT_PATTERN.matcher(lineTrimmed);
        matcher.find();
        String value = matcher.group(1);
        Comment comment = new Comment(value);
        return comment;
    }

}
