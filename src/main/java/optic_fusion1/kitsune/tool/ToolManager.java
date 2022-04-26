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
package optic_fusion1.kitsune.tool;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ToolManager {

    private static final HashMap<String, Tool> TOOLS = new HashMap<>();

    public Collection<Tool> getTools() {
        return Collections.unmodifiableCollection(TOOLS.values());
    }

    public Tool getTool(String name) {
        return TOOLS.get(name);
    }

    public void addTool(Tool tool) {
        if (tool == null) {
            return;
        }
        TOOLS.putIfAbsent(tool.getName(), tool);
    }

}
