package optic_fusion1.jre.tool;

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
