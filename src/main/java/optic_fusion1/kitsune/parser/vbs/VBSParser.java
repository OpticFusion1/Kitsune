/*
* Copyright (C) 2022 Optic_Fusion1
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package optic_fusion1.kitsune.parser.vbs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.vbs.domain.Comment;
import optic_fusion1.kitsune.parser.vbs.domain.ConstStatement;
import optic_fusion1.kitsune.parser.vbs.domain.Constants;
import optic_fusion1.kitsune.parser.vbs.domain.ElseIfStatement;
import optic_fusion1.kitsune.parser.vbs.domain.ElseStatement;
import optic_fusion1.kitsune.parser.vbs.domain.EmptyLineStatement;
import optic_fusion1.kitsune.parser.vbs.domain.Function;
import optic_fusion1.kitsune.parser.vbs.domain.IfStatement;
import optic_fusion1.kitsune.parser.vbs.domain.LoopStatement;
import optic_fusion1.kitsune.parser.vbs.domain.MsgBoxStatement;
import optic_fusion1.kitsune.parser.vbs.domain.SetStatement;
import optic_fusion1.kitsune.parser.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.vbs.domain.VBStatement;
import optic_fusion1.kitsune.parser.vbs.domain.VariableInit;
import optic_fusion1.kitsune.parser.vbs.interfaces.IContainer;
import optic_fusion1.kitsune.parser.vbs.util.Utils;

// TODO: Re-write this to have a tokenized Lexer & Parser, while still creating the necessary classes in optic_fusion1.kitsune.parser.vbs.domain.
// Projects to look at are:
// https://github.com/IraKorshunova/JavaCompiler
// https://github.com/Dapqu/Programming-Language/
// https://github.com/mohllal/semantic-analyzer
public class VBSParser {

    private static final boolean DEBUG = true;
    private VBSFile vbsFile;

    public void parse(VBSFile vbsFile) {
        try {
            checkFilExistance(vbsFile.file());
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find the file: " + vbsFile.file(), e);
            return;
        }
        this.vbsFile = vbsFile;

        List<String> sourceLines = Utils.getLines(vbsFile.file());
        vbsFile.setSourceLines(sourceLines);
        for (int i = 0; i < sourceLines.size(); i++) {
            String line = sourceLines.get(i);
            identifyAndConvert(i, line);
        }
    }

    private void identifyAndConvert(final int index, final String line) {
        String lineTrimmed = line.trim();
        if (lineTrimmed.toLowerCase().startsWith(Constants.FUNCTION_IDENTIFIER)) {
            Function func = StatementFactory.buildFunctionStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            func.setParent(parent);
            parent.getStatements().add(func);
            parent.getChildContainers().add(func);
            vbsFile.getContainerStack().add(func);
            vbsFile.getParsedStatements().add(func);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.DIM_IDENTIFIER)) {
            VariableInit vinit = StatementFactory.buildVariableInitStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            vinit.setParent(parent);
            parent.getStatements().add(vinit);
            vbsFile.getParsedStatements().add(vinit);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.WHILE_IDENTIFIER)
                || lineTrimmed.toLowerCase().startsWith(Constants.DO_WHILE_IDENTIFIER)
                || lineTrimmed.toLowerCase().startsWith(Constants.FOR_IDENTIFIER)) {
            LoopStatement loop = StatementFactory.buildLoopStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            loop.setParent(parent);
            parent.getStatements().add(loop);
            parent.getChildContainers().add(loop);
            vbsFile.getContainerStack().add(loop);
            vbsFile.getParsedStatements().add(loop);
        } else if (lineTrimmed.toLowerCase().matches(Constants.END_LOOP_REGEX)) {
            VBStatement end = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            end.setParentContainer(parent);
            parent.getStatements().add(end);
            popLastContainerFromStack();
            vbsFile.getParsedStatements().add(end);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.IF_IDENTIFIER)) {
            IfStatement ifstmt = StatementFactory.buildIFStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            ifstmt.setParent(parent);
            parent.getStatements().add(ifstmt);
            parent.getChildContainers().add(ifstmt);
            vbsFile.getContainerStack().add(ifstmt);
            vbsFile.getParsedStatements().add(ifstmt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.ELSE_IF_IDENTIFIER)) {
            popLastContainerFromStack();
            ElseIfStatement elseifstmt = StatementFactory.buildElseIFStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            elseifstmt.setParent(parent);
            parent.getStatements().add(elseifstmt);
            parent.getChildContainers().add(elseifstmt);
            vbsFile.getContainerStack().add(elseifstmt);
            vbsFile.getParsedStatements().add(elseifstmt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.ELSE_IDENTIFIER)) {
            popLastContainerFromStack();
            ElseStatement elsestmt = StatementFactory.buildElseStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            elsestmt.setParent(parent);
            parent.getStatements().add(elsestmt);
            parent.getChildContainers().add(elsestmt);
            vbsFile.getContainerStack().add(elsestmt);
            vbsFile.getParsedStatements().add(elsestmt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.END_IF_REGEX)) {
            popLastContainerFromStack();
            VBStatement end = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            end.setParentContainer(parent);
            parent.getStatements().add(end);
            vbsFile.getParsedStatements().add(end);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.END_FUNCTION_IDENTIFIER)) {
            VBStatement end = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            end.setParentContainer(parent);
            parent.getStatements().add(end);
            // Regex meaning: 
            //	- end matches the characters end literally (case sensitive)
            //	- <space>+ matches the character <space> literally
            //	- [a-zA-Z]+ match a single or multiple characters present
            //	- $ assert position at end of the string
            Pattern pattern = Pattern.compile("end +[a-zA-Z]+$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                popLastContainerFromStack();
            }
            vbsFile.getParsedStatements().add(end);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.MSG_IDENTIFIER) || lineTrimmed.toLowerCase().startsWith(Constants.MSGBOX_IDENTIFIER)) {
            MsgBoxStatement msgBox = StatementFactory.buildMsgBoxStatement(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            msgBox.setParent(parent);
            parent.getStatements().add(msgBox);
            vbsFile.getParsedStatements().add(msgBox);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.SET_IDENTIFIER)) {
            SetStatement setStatement = StatementFactory.buildSetStatmenets(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            setStatement.setParent(parent);
            parent.getStatements().add(setStatement);
            vbsFile.getParsedStatements().add(setStatement);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.CONST_IDENTIFIER)) {
            ConstStatement statement = StatementFactory.buildConstStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            statement.setParent(parent);
            parent.getStatements().add(statement);
            vbsFile.getParsedStatements().add(statement);
            vbsFile.getConstantPool().addStatement(statement);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.COMMENT_IDENTIFIER)) {
            Comment comment = StatementFactory.buildComments(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            comment.setParent(parent);
            parent.getStatements().add(comment);
            vbsFile.getParsedStatements().add(comment);
        } else if (lineTrimmed.isEmpty()) {
            EmptyLineStatement emptystmt = new EmptyLineStatement();
            emptystmt.setText("");
            emptystmt.setLineNumber(index);
            IContainer parent = getLastContainerFromStack();
            emptystmt.setParent(parent);
            parent.getStatements().add(emptystmt);
            vbsFile.getParsedStatements().add(emptystmt);
        } else {
            if (DEBUG) {
                LOGGER.warn("The following line does not get parsed: " + lineTrimmed);
            }
            VBStatement gstmt = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            gstmt.setParentContainer(parent);
            parent.getStatements().add(gstmt);
            vbsFile.getParsedStatements().add(gstmt);
        }
    }

    private void checkFilExistance(File vbsFile) throws FileNotFoundException {
        if (!vbsFile.exists()) {
            throw new FileNotFoundException("File not found. " + vbsFile);
        }
    }

    private IContainer getLastContainerFromStack() {
        List<IContainer> containerStack = vbsFile.getContainerStack();
        return containerStack.get(containerStack.size() - 1);
    }

    private void popLastContainerFromStack() {
        List<IContainer> containerStack = vbsFile.getContainerStack();
        if (containerStack.size() > 1) {
            containerStack.remove(containerStack.size() - 1);
        }
    }

    public List<String> getParsedStatementLines() {
        List<String> parsedLines = new ArrayList<>();
        parsedLines = visitContainersAndExtractRecursively(parsedLines, vbsFile.fileContainer());
        return parsedLines;
    }

    private List<String> visitContainersAndExtractRecursively(List<String> stmtList, IContainer container) {
        List<String> parsedStmtList = stmtList;
        List<Statement> statementsInContainer = container.getStatements();
        for (Statement st : statementsInContainer) {
            if (st instanceof IContainer iContainer) {
                parsedStmtList.add(st.getText());
                visitContainersAndExtractRecursively(parsedStmtList, iContainer);
            } else {
                parsedStmtList.add(st.getText());
            }
        }
        return parsedStmtList;

    }

    public VBSFile vbsFile() {
        return vbsFile;
    }

}
