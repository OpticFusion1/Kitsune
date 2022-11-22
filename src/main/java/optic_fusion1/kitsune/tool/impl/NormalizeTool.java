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
package optic_fusion1.kitsune.tool.impl;

import java.util.List;
import static optic_fusion1.kitsune.Kitsune.LOGGER;
import optic_fusion1.kitsune.tool.Tool;
import static optic_fusion1.kitsune.util.I18n.tl;
import optic_fusion1.kitsune.util.Utils;
import org.apache.commons.codec.digest.DigestUtils;

public class NormalizeTool extends Tool {

    private boolean showSha1Hash;

    public NormalizeTool() {
        super("normalize", tl("nt_desc"));
    }

    @Override
    public void run(List<String> args) {
        // TODO: Add better arg handling
        if (args.isEmpty()) {
            LOGGER.info(tl("not_enough_args") + " " + tl("nt_usage"));
            return;
        }
        if (args.contains("--showSHA1Hash")) {
            args.remove("--showSHA1Hash");
            showSha1Hash = true;
        }
        String string = Utils.normalize(args.get(0));
        String sha1Hash = "";
        if (showSha1Hash) {
            sha1Hash = DigestUtils.sha1Hex(string);
        }
        LOGGER.info(string + (showSha1Hash ? ": " + sha1Hash : ""));
    }

}
