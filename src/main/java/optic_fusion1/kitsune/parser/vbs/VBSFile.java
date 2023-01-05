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
import java.util.ArrayList;
import java.util.List;
import optic_fusion1.kitsune.parser.vbs.domain.ConstantPool;
import optic_fusion1.kitsune.parser.vbs.domain.FileContainer;
import optic_fusion1.kitsune.parser.vbs.domain.Statement;
import optic_fusion1.kitsune.parser.vbs.interfaces.IContainer;

public class VBSFile {

    private static final ConstantPool CONSTANT_POOL = new ConstantPool();
    private File file;
    private FileContainer fileContainer;
    private List<IContainer> containerStack = new ArrayList<>();
    private List<String> sourceLines = new ArrayList<>();
    private List<Statement> parsedStatements = new ArrayList<>();

    public VBSFile(File file) {
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
