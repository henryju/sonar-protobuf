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
package org.sonar.protobuf.parser;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.protobuf.api.ProtoBufKeyword;
import org.sonar.protobuf.api.ProtoBufPunctuator;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

public enum ProtoBufLexicalGrammar implements GrammarRuleKey {
  PROTOBUF_UNIT,

  /**
   * Lexical
   */
  EOF,
  INTEGER_LITERAL,
  STRING_LITERAL,

  /**
   * SPACING
   */
  SPACING,

  WHITESPACES,

  KEYWORDS,

  IDENTIFIER,

  SYNTAX,

  MESSAGE;

  public static LexerlessGrammar createGrammar() {
    return createGrammarBuilder().build();
  }

  public static LexerlessGrammarBuilder createGrammarBuilder() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    lexical(b);
    punctuators(b);
    keywords(b);

    return b;
  }

  public static void lexical(LexerlessGrammarBuilder b) {
    b.rule(SPACING).is(
      b.skippedTrivia(b.regexp("[" + LexicalConstant.LINE_TERMINATOR + LexicalConstant.WHITESPACE + "]*+")),
      b.zeroOrMore(
        b.commentTrivia(b.regexp(LexicalConstant.COMMENT)),
        b.skippedTrivia(b.regexp("[" + LexicalConstant.LINE_TERMINATOR + LexicalConstant.WHITESPACE + "]*+"))))
      .skip();

    // Identifier
    b.rule(WHITESPACES).is(b.regexp("[" + LexicalConstant.WHITESPACE + "]*+"));

    b.rule(IDENTIFIER).is(SPACING, b.nextNot(KEYWORDS), b.regexp(LexicalConstant.IDENTIFIER));

    b.rule(EOF).is(b.token(GenericTokenType.EOF, b.endOfInput())).skip();

    b.rule(INTEGER_LITERAL).is(SPACING, b.regexp(LexicalConstant.INTEGER_LITERAL));
    b.rule(STRING_LITERAL).is(SPACING, b.regexp(LexicalConstant.STRING_LITERAL));
  }

  private static void keywords(LexerlessGrammarBuilder b) {
    Object[] rest = new Object[ProtoBufKeyword.values().length - 2];

    for (int i = 0; i < ProtoBufKeyword.values().length; i++) {
      ProtoBufKeyword tokenType = ProtoBufKeyword.values()[i];

      b.rule(tokenType).is(SPACING, b.token(tokenType, tokenType.getValue())).skip();
      if (i > 1) {
        rest[i - 2] = b.regexp("(?i)" + tokenType.getValue());
      }
    }

    b.rule(KEYWORDS).is(SPACING,
      b.firstOf(
        ProtoBufKeyword.getKeywordValues()[0],
        ProtoBufKeyword.getKeywordValues()[1],
        rest));
  }

  private static void punctuators(LexerlessGrammarBuilder b) {
    for (ProtoBufPunctuator p : ProtoBufPunctuator.values()) {
      b.rule(p).is(SPACING, b.token(p, p.getValue())).skip();
    }
  }

}
