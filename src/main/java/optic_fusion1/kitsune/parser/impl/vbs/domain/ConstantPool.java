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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ConstantPool {

    private static final HashMap<String, ConstStatement> STATEMENTS = new HashMap<>();

    public Collection<ConstStatement> getStatements() {
        return Collections.unmodifiableCollection(STATEMENTS.values());
    }

    public ConstStatement getStatement(String name) {
        return STATEMENTS.get(name);
    }

    public void addStatement(ConstStatement stmnt) {
        STATEMENTS.putIfAbsent(stmnt.getName(), stmnt);
    }

}
