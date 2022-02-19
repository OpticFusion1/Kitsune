package optic_fusion1.jre;

import optic_fusion1.jre.shellparser.ParseException;
import optic_fusion1.jre.shellparser.ShellParser;
import optic_fusion1.jre.tool.Tool;
import optic_fusion1.jre.tool.ToolManager;
import optic_fusion1.jre.tool.impl.analyze.AnalyzeTool;
import optic_fusion1.jre.tool.impl.StringsTool;
import java.util.List;
import java.util.Scanner;

public class JRE extends Thread {

    private static final ToolManager TOOL_MANAGER = new ToolManager();
    private static final Scanner SCANNER = new Scanner(System.in);
    private boolean running;

    @Override
    public void run() {
        init();
        registerTools(new StringsTool(), new AnalyzeTool());
        System.out.println("Program loaded. Enter a command:");
        handleCLI();
    }

    private void handleCLI() {
        while (running) {
            try {
                List<String> args = ShellParser.parseString(SCANNER.nextLine());
                if (args.isEmpty()) {
                    System.out.println("You did not enter any arguments");
                    continue;
                }
                Tool tool = TOOL_MANAGER.getTool(args.get(0));
                if (tool == null) {
                    System.out.println(args.get(0) + " is not a valid tool");
                    continue;
                }
                args.remove(0);
                tool.run(args);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void init() {
        running = true;
    }

    private void registerTools(Tool...tools) {
        for (Tool tool : tools) {
            TOOL_MANAGER.addTool(tool);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public ToolManager getToolManager() {
        return TOOL_MANAGER;
    }

}
