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

import com.sonar.sslr.api.typed.GrammarBuilder;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldRuleTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldScalarTypeTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.protobuf.api.ProtoBufKeyword;
import org.sonar.protobuf.api.ProtoBufPunctuator;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class ProtoBufGrammar {

  private final GrammarBuilder<InternalSyntaxToken> b;
  private final TreeFactory f;

  public ProtoBufGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
    this.b = b;
    this.f = f;
  }

  public ProtoBufUnitTree PROTOBUF_UNIT() {
    return b.<ProtoBufUnitTree>nonterminal(ProtoBufLexicalGrammar.PROTOBUF_UNIT).is(
      f.protoBufUnit(b.optional(SYNTAX()), b.zeroOrMore(MESSAGE()),
        b.optional(b.token(ProtoBufLexicalGrammar.SPACING)), b.token(ProtoBufLexicalGrammar.EOF)));
  }

  public SyntaxTree SYNTAX() {
    return b.<SyntaxTree>nonterminal(ProtoBufLexicalGrammar.SYNTAX).is(
      f.syntax(b.optional(b.token(ProtoBufLexicalGrammar.SPACING)),
        b.token(ProtoBufKeyword.SYNTAX),
        b.token(ProtoBufPunctuator.EQU),
        b.token(ProtoBufLexicalGrammar.STRING_LITERAL),
        b.token(ProtoBufPunctuator.SEMICOLON)));
  }

  public MessageTree MESSAGE() {
    return b.<MessageTree>nonterminal(ProtoBufLexicalGrammar.MESSAGE).is(
      f.message(b.optional(b.token(ProtoBufLexicalGrammar.SPACING)),
        b.token(ProtoBufKeyword.MESSAGE),
        IDENTIFIER(),
        b.token(ProtoBufPunctuator.LCURLYBRACE),
        b.zeroOrMore(b.firstOf(FIELD(), MESSAGE())),
        b.token(ProtoBufPunctuator.RCURLYBRACE)));
  }

  public FieldTree FIELD() {
    return b.<FieldTree>nonterminal(ProtoBufLexicalGrammar.FIELD).is(
      f.field(b.optional(b.token(ProtoBufLexicalGrammar.SPACING)),
        b.optional(FIELD_RULE()),
        FIELD_SCALAR_TYPE(),
        IDENTIFIER(),
        b.token(ProtoBufPunctuator.EQU),
        b.token(ProtoBufLexicalGrammar.INTEGER_LITERAL),
        b.token(ProtoBufPunctuator.SEMICOLON)));
  }

  public IdentifierTree IDENTIFIER() {
    return b.<IdentifierTree>nonterminal().is(
      f.identifier(b.token(ProtoBufLexicalGrammar.IDENTIFIER)));
  }

  public FieldScalarTypeTree FIELD_SCALAR_TYPE() {
    return b.<FieldScalarTypeTree>nonterminal(ProtoBufLexicalGrammar.FIELD_SCALAR_TYPE)
      .is(
        f.fieldScalarType(
          b.firstOf(
            b.token(ProtoBufKeyword.DOUBLE),
            b.token(ProtoBufKeyword.FLOAT),
            b.token(ProtoBufKeyword.INT32),
            b.token(ProtoBufKeyword.INT64),
            b.token(ProtoBufKeyword.UINT32),
            b.token(ProtoBufKeyword.UINT64),
            b.token(ProtoBufKeyword.SINT32),
            b.token(ProtoBufKeyword.SINT64),
            b.token(ProtoBufKeyword.FIXED32),
            b.token(ProtoBufKeyword.FIXED64),
            b.token(ProtoBufKeyword.SFIXED32),
            b.token(ProtoBufKeyword.SFIXED64),
            b.token(ProtoBufKeyword.BOOL),
            b.token(ProtoBufKeyword.STRING),
            b.token(ProtoBufKeyword.BYTES))));
  }

  public FieldRuleTree FIELD_RULE() {
    return b.<FieldRuleTree>nonterminal(ProtoBufLexicalGrammar.FIELD_RULE)
      .is(
        f.fieldRule(
          b.firstOf(
            b.token(ProtoBufKeyword.REQUIRED),
            b.token(ProtoBufKeyword.OPTIONAL),
            b.token(ProtoBufKeyword.REPEATED))));
  }

}
