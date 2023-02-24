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
package optic_fusion1.kitsune.parser.impl.vbs.domain;

public enum DataType {

    // TODO: Look to see if there's any other DataTypes that should be added to this
    STRING("String"),
    INT("Int"),
    VAR("var"),
    BY_VAL("ByVal"),
    BY_REF("ByRef"),
    SINGLE("Single"),
    EMPTY("");

    private final String name;

    private DataType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    @Override
    public String toString() {
        return name;
    }

    public static DataType fromString(String text) {
        if (text != null) {
            for (DataType b : DataType.values()) {
                if (text.equalsIgnoreCase(b.name)) {
                    return b;
                }
            }
        }
        return VAR;
    }
}
