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
package optic_fusion1.kitsune.parser.impl.vbs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.Parser;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Comment;
import optic_fusion1.kitsune.parser.impl.vbs.domain.ConstStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Constants;
import optic_fusion1.kitsune.parser.impl.vbs.domain.ElseIfStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.ElseStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.EmptyLineStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Function;
import optic_fusion1.kitsune.parser.impl.vbs.domain.IfStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.LoopStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.MsgBoxStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.SetStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.VariableInit;
import optic_fusion1.kitsune.parser.impl.vbs.interfaces.IContainer;

// TODO: Re-write this to have a tokenized Lexer & Parser, while still creating the necessary classes in optic_fusion1.kitsune.parser.vbs.domain.
// Projects to look at are:
// https://github.com/IraKorshunova/JavaCompiler
// https://github.com/Dapqu/Programming-Language/
// https://github.com/mohllal/semantic-analyzer
// https://github.com/jaredprince/Newt
public class VBSParser extends Parser {

    @Override
    public void identifyAndConvert(final int index, final String line) {
        String lineTrimmed = line.trim();
        if (lineTrimmed.toLowerCase().startsWith(Constants.FUNCTION_IDENTIFIER)) {
            Function func = StatementFactory.buildFunctionStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            func.setParent(parent);
            parent.getStatements().add(func);
            parent.getChildContainers().add(func);
            parsedFile().getContainerStack().add(func);
            parsedFile().getParsedStatements().add(func);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.DIM_IDENTIFIER)) {
            VariableInit vinit = StatementFactory.buildVariableInitStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            vinit.setParent(parent);
            parent.getStatements().add(vinit);
            parsedFile().getParsedStatements().add(vinit);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.WHILE_IDENTIFIER)
                || lineTrimmed.toLowerCase().startsWith(Constants.DO_WHILE_IDENTIFIER)
                || lineTrimmed.toLowerCase().startsWith(Constants.FOR_IDENTIFIER)) {
            LoopStatement loop = StatementFactory.buildLoopStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            loop.setParent(parent);
            parent.getStatements().add(loop);
            parent.getChildContainers().add(loop);
            parsedFile().getContainerStack().add(loop);
            parsedFile().getParsedStatements().add(loop);
        } else if (lineTrimmed.toLowerCase().matches(Constants.END_LOOP_REGEX)) {
            Statement end = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            end.setParent(parent);
            parent.getStatements().add(end);
            popLastContainerFromStack();
            parsedFile().getParsedStatements().add(end);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.IF_IDENTIFIER)) {
            IfStatement ifstmt = StatementFactory.buildIFStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            ifstmt.setParent(parent);
            parent.getStatements().add(ifstmt);
            parent.getChildContainers().add(ifstmt);
            parsedFile().getContainerStack().add(ifstmt);
            parsedFile().getParsedStatements().add(ifstmt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.ELSE_IF_IDENTIFIER)) {
            popLastContainerFromStack();
            ElseIfStatement elseifstmt = StatementFactory.buildElseIFStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            elseifstmt.setParent(parent);
            parent.getStatements().add(elseifstmt);
            parent.getChildContainers().add(elseifstmt);
            parsedFile().getContainerStack().add(elseifstmt);
            parsedFile().getParsedStatements().add(elseifstmt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.ELSE_IDENTIFIER)) {
            popLastContainerFromStack();
            ElseStatement elsestmt = StatementFactory.buildElseStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            elsestmt.setParent(parent);
            parent.getStatements().add(elsestmt);
            parent.getChildContainers().add(elsestmt);
            parsedFile().getContainerStack().add(elsestmt);
            parsedFile().getParsedStatements().add(elsestmt);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.END_IF_IDENTIFIER)) {
            popLastContainerFromStack();
            Statement end = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            end.setParent(parent);
            parent.getStatements().add(end);
            parsedFile().getParsedStatements().add(end);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.END_FUNCTION_IDENTIFIER)) {
            Statement end = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            end.setParent(parent);
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
            parsedFile().getParsedStatements().add(end);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.MSG_IDENTIFIER) || lineTrimmed.toLowerCase().startsWith(Constants.MSGBOX_IDENTIFIER)) {
            MsgBoxStatement msgBox = StatementFactory.buildMsgBoxStatement(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            msgBox.setParent(parent);
            parent.getStatements().add(msgBox);
            parsedFile().getParsedStatements().add(msgBox);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.SET_IDENTIFIER)) {
            SetStatement setStatement = StatementFactory.buildSetStatmenets(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            setStatement.setParent(parent);
            parent.getStatements().add(setStatement);
            parsedFile().getParsedStatements().add(setStatement);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.CONST_IDENTIFIER)) {
            ConstStatement statement = StatementFactory.buildConstStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            statement.setParent(parent);
            parent.getStatements().add(statement);
            parsedFile().getParsedStatements().add(statement);
            parsedFile().getConstantPool().addStatement(statement);
        } else if (lineTrimmed.toLowerCase().startsWith(Constants.COMMENT_IDENTIFIER)) {
            Comment comment = StatementFactory.buildComments(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            comment.setParent(parent);
            parent.getStatements().add(comment);
            parsedFile().getParsedStatements().add(comment);
        } else if (lineTrimmed.isEmpty()) {
            EmptyLineStatement emptystmt = new EmptyLineStatement();
            emptystmt.setText("");
            emptystmt.setLineNumber(index);
            IContainer parent = getLastContainerFromStack();
            emptystmt.setParent(parent);
            parent.getStatements().add(emptystmt);
            parsedFile().getParsedStatements().add(emptystmt);
        } else {
            if (DEBUG) {
                LOGGER.warn("The following line does not get parsed: " + lineTrimmed);
            }
            Statement gstmt = StatementFactory.buildGenericStatements(index, lineTrimmed);
            IContainer parent = getLastContainerFromStack();
            gstmt.setParent(parent);
            parent.getStatements().add(gstmt);
            parsedFile().getParsedStatements().add(gstmt);
        }
    }

}
