package optic_fusion1.kitsune.parser.vbs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;

public class Utils {

    public static List<Statement> filterStatements(List<Statement> list, StatementFilter filter) {

        List<Statement> result = new ArrayList<>();

        for (Statement item : list) {
            if (item.getType().equals(filter.getFilterClass())) {
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
