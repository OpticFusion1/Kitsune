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
package optic_fusion1.kitsune.parser.vbs.util;

import optic_fusion1.kitsune.parser.vbs.domain.Statement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import optic_fusion1.kitsune.parser.vbs.StatementFilter;
import static optic_fusion1.kitsune.Kitsune.LOGGER;

public class Utils {

    public static List<Statement> filterStatements(List<Statement> list, StatementFilter filter) {
        List<Statement> result = new ArrayList<>();
        for (Statement item : list) {
            if (item.getType().equals(filter.filterClass())) {
                result.add(item);
            }
        }
        return result;
    }

    public static List<String> getLines(File inputfile) {
        List<String> lines = new ArrayList<>();
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(inputfile));
            while ((sCurrentLine = br.readLine()) != null) {
                lines.add(sCurrentLine);
            }
        } catch (IOException e) {
            LOGGER.error("Exception", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                LOGGER.error("Exception", ex);
            }
        }
        return lines;
    }

    public static void writeLinesToFile(List<String> lines, File outputfile) {
        BufferedWriter bw = null;
        try {
            StringBuilder builder = new StringBuilder();
            // if file doesnt exists, then create it
            if (!outputfile.exists()) {
                outputfile.createNewFile();
            }
            for (String line : lines) {
                builder.append(line).append("\n");
            }
            FileWriter fw = new FileWriter(outputfile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(builder.toString());
            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            LOGGER.error("Exception", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                LOGGER.error("Exception", ex);
            }
        }
    }

}
