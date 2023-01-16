package optic_fusion1.kitsune.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.impl.vbs.interfaces.IContainer;
import optic_fusion1.kitsune.parser.impl.vbs.util.Utils;

public abstract class Parser {

    public static final boolean DEBUG = false;
    public ParsedFile parsedFile;

    public void parse(ParsedFile parsedFile) {
        try {
            checkFileExistance(parsedFile.file());
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find the file: " + parsedFile.file(), e);
            return;
        }
        this.parsedFile = parsedFile;

        List<String> sourceLines = Utils.getLines(parsedFile.file());
        parsedFile.setSourceLines(sourceLines);
        for (int i = 0; i < sourceLines.size(); i++) {
            String line = sourceLines.get(i);
            identifyAndConvert(i, line);
        }
    }

    public abstract void identifyAndConvert(int index, String line);

    public void checkFileExistance(File parsedFile) throws FileNotFoundException {
        if (!parsedFile.exists()) {
            throw new FileNotFoundException("File '" + parsedFile + "' not found");
        }
    }

    public ParsedFile parsedFile() {
        return parsedFile;
    }

    public IContainer getLastContainerFromStack() {
        List<IContainer> containerStack = parsedFile().getContainerStack();
        return containerStack.get(containerStack.size() - 1);
    }

    public void popLastContainerFromStack() {
        List<IContainer> containerStack = parsedFile().getContainerStack();
        if (containerStack.size() > 1) {
            containerStack.remove(containerStack.size() - 1);
        }
    }

    public List<String> getParsedStatementLines() {
        List<String> parsedLines = new ArrayList<>();
        parsedLines = visitContainersAndExtractRecursively(parsedLines, parsedFile().fileContainer());
        return parsedLines;
    }

    public List<String> visitContainersAndExtractRecursively(List<String> stmtList, IContainer container) {
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

}
