/*
 * SonarQube Protocol Buffers Plugin
 * Copyright (C) 2015 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.protobuf.grammar;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;

public enum ProtobufGrammar implements GrammarRuleKey {
  DOCUMENT,

  S,

  SYNTAX_DECL,
  SYNTAX_KEYWORD,
  EQUAL,
  SEMICOLON,

  MESSAGE_DECL,
  MESSAGE_KEYWORD,
  MESSAGE_NAME,
  L_BRACE,
  R_BRACE;

  private static final String CHAR_REGEXP = "[\u0001-\uD7FF\uE000-\uFFFD]";

  private static final String S_REGEXP = "[ \t\r\n]++";
  private static final String NAME_START_CHAR_REGEXP = "[:A-Z_a-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D" +
    "\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD]";
  private static final String NAME_CHAR_REGEXP = "(?:" + NAME_START_CHAR_REGEXP + "|[-.0-9\u00B7\u0300-\u036F\u203F-\u2040])";
  private static final String NAME_REGEXP = "(?:" + NAME_START_CHAR_REGEXP + NAME_CHAR_REGEXP + "*+" + ")";
  private static final String NM_TOKEN_REGEXP = "(?:" + NAME_CHAR_REGEXP + "++)";
  private static final String PUBID_CHAR_REGEXP = "[ \r\na-zA-Z0-9-'()+,./:=?;!*#@$_%]";

  private static final String COMMENT_REGEXP = "<!--" + "(?:(?!-)" + CHAR_REGEXP + "|-" + "(?!-)" + CHAR_REGEXP + ")*+" + "-->";

  private static final String CHAR_REF_REGEXP = "(?:" + "&#" + "(?:[0-9]++|x[0-9a-fA-F]++);" + ")";

  private static final String ENC_NAME_REGEXP = "(?:[A-Za-z][A-Za-z0-9._-]++)";

  public static LexerlessGrammarBuilder createGrammarBuilder() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    b.rule(S).is(b.skippedTrivia(b.regexp(S_REGEXP))).skip();

    b.rule(SYNTAX_KEYWORD).is("syntax");
    b.rule(MESSAGE_KEYWORD).is("message");
    b.rule(L_BRACE).is("{");
    b.rule(R_BRACE).is("}");

    b.rule(EQUAL).is("=");

    b.rule(MESSAGE_NAME).is(b.regexp("[a-zA-Z0-9]+"));
    b.rule(SEMICOLON).is(";");

    b.rule(SYNTAX_DECL).is(SYNTAX_KEYWORD, b.optional(S), EQUAL, b.optional(S), b.sequence("\"", b.regexp("[^\"]+"), "\""), b.optional(S), SEMICOLON);

    b.rule(MESSAGE_DECL).is(MESSAGE_KEYWORD, S, MESSAGE_NAME, b.optional(S), L_BRACE, b.optional(S), R_BRACE);

    b.rule(DOCUMENT).is(SYNTAX_DECL, b.zeroOrMore(MESSAGE_DECL));

    b.setRootRule(DOCUMENT);

    return b;
  }

}
