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

import com.sonar.sslr.api.typed.Optional;
import java.util.Collections;
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldRuleTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldScalarTypeTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.protobuf.tree.impl.FieldTreeImpl;
import org.sonar.protobuf.tree.impl.MessageTreeImpl;
import org.sonar.protobuf.tree.impl.ProtoBufUnitTreeImpl;
import org.sonar.protobuf.tree.impl.SyntaxTreeImpl;
import org.sonar.protobuf.tree.impl.expression.FieldRuleTreeImpl;
import org.sonar.protobuf.tree.impl.expression.FieldScalarTypeTreeImpl;
import org.sonar.protobuf.tree.impl.expression.IdentifierTreeImpl;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class TreeFactory {

  public ProtoBufUnitTree protoBufUnit(Optional<SyntaxTree> syntaxTree, Optional<List<MessageTree>> messages, Optional<InternalSyntaxToken> spacing, InternalSyntaxToken eofToken) {
    return new ProtoBufUnitTreeImpl(syntaxTree.orNull(), optionalList(messages), eofToken);
  }

  public MessageTree message(Optional<InternalSyntaxToken> spacing, InternalSyntaxToken messageToken, IdentifierTree name, InternalSyntaxToken lcurlyToken,
    Optional<List<Tree>> fieldOrMessage, InternalSyntaxToken rcurlyToken) {
    return new MessageTreeImpl(messageToken, name, lcurlyToken, optionalList(fieldOrMessage), rcurlyToken);
  }

  public IdentifierTree identifier(InternalSyntaxToken token) {
    return new IdentifierTreeImpl(token);
  }

  public SyntaxTree syntax(Optional<InternalSyntaxToken> spacing, InternalSyntaxToken token, InternalSyntaxToken eq, InternalSyntaxToken value, InternalSyntaxToken colon) {
    return new SyntaxTreeImpl(token, eq, value, colon);
  }

  public FieldScalarTypeTree fieldScalarType(InternalSyntaxToken token) {
    return new FieldScalarTypeTreeImpl(token);
  }

  public FieldTree field(Optional<InternalSyntaxToken> spacing, Optional<FieldRuleTree> rule, FieldScalarTypeTree type, IdentifierTree identifier, InternalSyntaxToken eq,
    InternalSyntaxToken tag,
    InternalSyntaxToken colon) {
    return new FieldTreeImpl(rule.orNull(), type, identifier, eq, tag, colon);
  }

  public FieldRuleTree fieldRule(InternalSyntaxToken token) {
    return new FieldRuleTreeImpl(token);
  }

  private static <T extends Tree> List<T> optionalList(Optional<List<T>> list) {
    if (list.isPresent()) {
      return list.get();
    } else {
      return Collections.emptyList();
    }
  }

}
