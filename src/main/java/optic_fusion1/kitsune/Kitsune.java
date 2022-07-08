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
package optic_fusion1.kitsune;

import java.util.List;
import optic_fusion1.kitsune.tool.Tool;
import optic_fusion1.kitsune.tool.ToolManager;
import optic_fusion1.kitsune.tool.impl.analyze.AnalyzeTool;
import optic_fusion1.kitsune.tool.impl.StringsTool;
import java.util.Scanner;
import optic_fusion1.kitsune.logging.CustomLogger;
import optic_fusion1.kitsune.shellparser.ParseException;
import optic_fusion1.kitsune.shellparser.ShellParser;

public class Kitsune implements Runnable {

    private static final ToolManager TOOL_MANAGER = new ToolManager();
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final CustomLogger LOGGER = new CustomLogger();
    private boolean running;

    @Override
    public void run() {
        init();
    }

    private void handleCLI() {
        while (running) {
            try {
                List<String> args = ShellParser.parseString(SCANNER.nextLine());
                if (args.isEmpty()) {
                    LOGGER.warn("You did not enter any arguments");
                    continue;
                }
                Tool tool = TOOL_MANAGER.getTool(args.get(0));
                if (tool == null) {
                    LOGGER.warn(args.get(0) + " is not a valid tool");
                    continue;
                }
                args.remove(0);
                tool.run(args);
            } catch (ParseException e) {
                LOGGER.exception(e);
            }
        }
    }

    private void init() {
        running = true;
        registerTools(new StringsTool(), new AnalyzeTool());
        LOGGER.info("Program loaded. Enter a command:");
        handleCLI();
    }

    private void registerTools(Tool... tools) {
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
