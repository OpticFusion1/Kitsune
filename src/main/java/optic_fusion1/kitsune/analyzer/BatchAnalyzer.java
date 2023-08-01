package optic_fusion1.kitsune.analyzer;

import java.io.File;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.ParsedFile;
import optic_fusion1.kitsune.parser.impl.batch.BatchParser;
import optic_fusion1.kitsune.parser.impl.batch.domain.StartStatement;
import optic_fusion1.kitsune.parser.impl.vbs.domain.Statement;

public class BatchAnalyzer extends Analyzer {

    @Override
    public void analyze(File input) {
        BatchParser parser = new BatchParser();
        parser.parse(new ParsedFile(input));
        for (Statement statement : parser.parsedFile().getParsedStatements()) {
            if (statement instanceof StartStatement stmnt) {
                LOGGER.info("Runs the program: " + stmnt.getFile());
            }

        }
    }

}
