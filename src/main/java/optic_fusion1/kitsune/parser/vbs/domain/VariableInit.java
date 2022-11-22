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
package optic_fusion1.kitsune.parser.vbs.domain;

import java.util.ArrayList;
import java.util.List;

public class VariableInit extends Statement {

    private String name;
    private DataType variableType;
    private List<String> variables = new ArrayList<>();

    public List<String> getVariables() {
        return variables;
    }

    @Override
    public Class<?> getType() {
        return this.getClass();
    }

    public DataType getVariableType() {
        return variableType;
    }

    public void setVariableType(DataType variableType) {
        this.variableType = variableType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
