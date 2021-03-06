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
package optic_fusion1.kitsune.shellparser;

import java.util.ArrayList;
import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.shellparser.state.StartState;

public final class ShellParser {

    private ShellParser() {
    }

    public static List<String> parseString(String string) throws ParseException {
        return new StartState().parse(string, "", new ArrayList<>(), null);
    }

    public static List<String> safeParseString(String string) {
        try {
            return parseString(string);
        } catch (ParseException e) {
            LOGGER.exception(e);
            return null;
        }
    }
}
