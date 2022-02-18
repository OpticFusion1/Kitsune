package optic_fusion1.jre.shellparser.state;

import java.util.List;
import optic_fusion1.jre.shellparser.ParseException;

public class QuoteState extends State {

    char quote;

    public QuoteState(char quote) {
        this.quote = quote;
    }

    @Override
    public List<String> parse(final String parsing, final String accumulator, final List<String> parsed, final State referrer) throws ParseException {
        if (parsing.isEmpty()) {
            throw new ParseException("Mismatched quote character: " + this.quote);
        }
        final char c = (char) parsing.getBytes()[0];
        if (c == '\\') {
            return new EscapeState().parse(parsing.substring(1), accumulator, parsed, this);
        }
        if (c == this.quote) {
            return new StartState().parse(parsing.substring(1), accumulator, parsed, this);
        }
        return new QuoteState(this.quote).parse(parsing.substring(1), accumulator + c, parsed, this);
    }
}
