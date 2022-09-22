package optic_fusion1.kitsune.tool.impl.analyze.analyzer.vbs;

import java.io.File;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.parser.vbs.Comment;
import optic_fusion1.kitsune.parser.vbs.Statement;
import optic_fusion1.kitsune.parser.vbs.VBSParser;
import optic_fusion1.kitsune.tool.impl.analyze.analyzer.Analyzer;

public class VBSAnalyzer extends Analyzer {

    @Override
    public void analyze(File input) {
        VBSParser parser = new VBSParser(input);
        parser.parse();
        for (Statement statement : parser.getParsedStatements()) {
            if (statement instanceof Comment comment) {
                LOGGER.info(comment.value());
            }
        }
    }

}
