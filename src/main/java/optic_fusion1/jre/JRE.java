package optic_fusion1.jre;

import optic_fusion1.jre.tool.ToolManager;

public class JRE extends Thread {

    private static final ToolManager TOOL_MANAGER = new ToolManager();
    private boolean running;

    @Override
    public void run() {
        init();
        while (running) {

        }
    }

    private void init() {
        running = true;
    }

    public boolean isRunning() {
        return running;
    }

    public ToolManager getToolManager() {
        return TOOL_MANAGER;
    }

}
