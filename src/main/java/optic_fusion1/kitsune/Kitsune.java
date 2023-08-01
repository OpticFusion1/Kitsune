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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;
import optic_fusion1.kitsune.analyzer.AnalyzerManager;
import optic_fusion1.kitsune.logging.CustomLogger;
import optic_fusion1.kitsune.shellparser.ParseException;
import optic_fusion1.kitsune.shellparser.ShellParser;
import optic_fusion1.kitsune.tool.Tool;
import optic_fusion1.kitsune.tool.ToolManager;
import optic_fusion1.kitsune.tool.impl.FixTool;
import optic_fusion1.kitsune.tool.impl.IdFetcherTool;
import optic_fusion1.kitsune.tool.impl.NormalizeTool;
import optic_fusion1.kitsune.tool.impl.StringsTool;
import optic_fusion1.kitsune.tool.impl.analyze.AnalyzeTool;
import optic_fusion1.kitsune.util.I18n;
import static optic_fusion1.kitsune.util.I18n.tl;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class Kitsune implements Runnable {

    private static final AnalyzerManager ANALYZER_MANAGER = new AnalyzerManager();
    private static final ToolManager TOOL_MANAGER = new ToolManager();
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final CustomLogger LOGGER = new CustomLogger();
    private boolean running;
    private static Kitsune INSTANCE;

    static {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
            System.setProperty("pf4j.pluginsDir", "kitsune/plugins");
            File pluginsDirectory = new File("kitsune", "plugins");
            if (!pluginsDirectory.exists()) {
                pluginsDirectory.mkdirs();
            }
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }
    }

    @Override
    public void run() {
        INSTANCE = this;
        init();
        LOGGER.info(tl("kitsune_program_loaded"));
        handleCLI();
    }

    private void handleCLI() {
        while (running) {
            try {
                List<String> args = ShellParser.parseString(SCANNER.nextLine());
                if (args.isEmpty()) {
                    LOGGER.warn(tl("not_enough_args"));
                    continue;
                }
                Tool tool = TOOL_MANAGER.getTool(args.get(0));
                if (tool == null) {
                    // TODO: Look into suggesting the closest tool to what's provided
                    LOGGER.warn(tl("kitsune_invalid_tool", args.get(0)));
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
        loadI18n();
        registerTools(new StringsTool(), new AnalyzeTool(ANALYZER_MANAGER), new FixTool(),
                new NormalizeTool(), new IdFetcherTool());

        PluginManager pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    private void loadI18n() {
        I18n I18n = new I18n();
        // TODO: Add other lang files
        I18n.updateLocale("en");
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

    public AnalyzerManager getAnalyzerManager() {
        return ANALYZER_MANAGER;
    }

    public static Kitsune getInstance() {
        return INSTANCE;
    }
    
}
