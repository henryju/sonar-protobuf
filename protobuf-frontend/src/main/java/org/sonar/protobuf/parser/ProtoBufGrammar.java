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
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.Tree.Kind;
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
      f.protoBufUnit(b.zeroOrMore(MESSAGE()), b.optional(b.token(ProtoBufLexicalGrammar.SPACING)), b.token(ProtoBufLexicalGrammar.EOF)));
  }

  public MessageTree MESSAGE() {
    return b.<MessageTree>nonterminal(ProtoBufLexicalGrammar.MESSAGE).is(
      f.message(b.token(ProtoBufKeyword.MESSAGE),
        IDENTIFIER(),
        b.token(ProtoBufPunctuator.LCURLYBRACE),
        b.token(ProtoBufPunctuator.RCURLYBRACE)));
  }

  public IdentifierTree IDENTIFIER() {
    return b.<IdentifierTree>nonterminal(Kind.IDENTIFIER).is(
      f.identifier(b.token(ProtoBufLexicalGrammar.IDENTIFIER)));
  }

}
