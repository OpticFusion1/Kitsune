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
package optic_fusion1.kitsune.shellparser.state;

import java.util.List;
import optic_fusion1.kitsune.shellparser.ParseException;
import static optic_fusion1.kitsune.util.I18n.tl;

public class QuoteState extends State {

    char quote;

    public QuoteState(char quote) {
        this.quote = quote;
    }

    @Override
    public List<String> parse(final String parsing, final String accumulator, final List<String> parsed, final State referrer) throws ParseException {
        if (parsing.isEmpty()) {
            throw new ParseException(tl("qs_parse_exception", quote));
        }
        final char c = (char) parsing.getBytes()[0];
        if (c == '\\') {
            return new EscapeState().parse(parsing.substring(1), accumulator, parsed, this);
        }
        if (c == quote) {
            return new StartState().parse(parsing.substring(1), accumulator, parsed, this);
        }
        return new QuoteState(quote).parse(parsing.substring(1), accumulator + c, parsed, this);
    }
}
