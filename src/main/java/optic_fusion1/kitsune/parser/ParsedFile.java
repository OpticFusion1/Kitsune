package optic_fusion1.kitsune.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import optic_fusion1.kitsune.parser.impl.vbs.domain.ConstantPool;
import optic_fusion1.kitsune.parser.impl.vbs.domain.FileContainer;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.impl.vbs.interfaces.IContainer;

public class ParsedFile {

    private static final ConstantPool CONSTANT_POOL = new ConstantPool();
    private File file;
    private FileContainer fileContainer;
    private List<IContainer> containerStack = new ArrayList<>();
    private List<String> sourceLines = new ArrayList<>();
    private List<Statement> parsedStatements = new ArrayList<>();

    public ParsedFile(File file) {
        this.file = file;
        fileContainer = new FileContainer(file.getName());
        containerStack.add(fileContainer);
    }

    public void setSourceLines(List<String> lines) {
        sourceLines = lines;
    }

    public List<String> getSourceLines() {
        return sourceLines;
    }

    public List<IContainer> getContainerStack() {
        return containerStack;
    }

    public List<Statement> getParsedStatements() {
        return parsedStatements;
    }

    public ConstantPool getConstantPool() {
        return CONSTANT_POOL;
    }

    public File file() {
        return file;
    }

    public FileContainer fileContainer() {
        return fileContainer;
    }

}
