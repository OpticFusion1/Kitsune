package optic_fusion1.jre.shellparser.state;

import optic_fusion1.jre.shellparser.ParseException;

import java.util.List;

public abstract class State {

    public abstract List<String> parse(String parsing, String accumulator, List<String> parsed, State referrer) throws ParseException;
}
