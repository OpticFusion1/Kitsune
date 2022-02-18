package optic_fusion1.jre.tool;

import java.util.List;

public abstract class Tool {

    private String name;
    private String description;

    public Tool(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void run(List<String> args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
