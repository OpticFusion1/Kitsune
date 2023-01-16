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
package optic_fusion1.kitsune.parser.impl.vbs.domain;

public final class Constants {

    public static final String FUNCTION_IDENTIFIER = "function";
    public static final String END_FUNCTION_IDENTIFIER = "end";
    public static final String DIM_IDENTIFIER = "dim";
    public static final String FOR_IDENTIFIER = "for";
    public static final String DO_WHILE_IDENTIFIER = "do while";
    public static final String WHILE_IDENTIFIER = "while";
    public static final String END_LOOP_REGEX = "(loop|next|wend)";
    public static final String IF_IDENTIFIER = "if";
    public static final String ELSE_IF_IDENTIFIER = "elseif";
    public static final String END_IF_IDENTIFIER = "end if";
    public static final String ELSE_IDENTIFIER = "else";
    public static final String MSG_IDENTIFIER = "msg";
    public static final String MSGBOX_IDENTIFIER = "msgbox";
    public static final String SET_IDENTIFIER = "set";
    public static final String CONST_IDENTIFIER = "Const";
    public static final String COMMENT_IDENTIFIER = "'";

    private Constants() {
    }
}
