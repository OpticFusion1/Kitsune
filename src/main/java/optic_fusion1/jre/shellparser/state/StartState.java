package optic_fusion1.jre.shellparser.state;

import java.util.List;
import optic_fusion1.jre.shellparser.ParseException;

public class StartState extends State {

    @Override
    public List<String> parse(String parsing, String accumulator, List<String> parsed, State referrer) throws ParseException {
        if (parsing.isEmpty()) {
            if (!accumulator.isEmpty()) {
                parsed.add(accumulator);
            }
            return parsed;
        }
        final char c = (char) parsing.getBytes()[0];
        switch (c) {
            case ' ' -> {
                if (!accumulator.isEmpty()) {
                    parsed.add(accumulator);
                }
                return new StartState().parse(parsing.substring(1), "", parsed, this);
            }
            case '\\' -> {
                return new EscapeState().parse(parsing.substring(1), accumulator, parsed, this);
            }
            case '\"', '\'' -> {
                return new QuoteState(c).parse(parsing.substring(1), accumulator, parsed, this);
            }
            default -> {
                return new StartState().parse(parsing.substring(1), accumulator + c, parsed, this);
            }
        }
    }
}