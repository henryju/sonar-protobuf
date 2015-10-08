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
package org.sonar.plugins.protobuf.api.tree;

import com.google.common.annotations.Beta;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.plugins.protobuf.api.tree.expression.FieldRuleTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldScalarTypeTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxTrivia;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.sslr.grammar.GrammarRuleKey;

/**
 * Common interface for all nodes in an abstract syntax tree.
 */
@Beta
public interface Tree {

  boolean is(Kind... kind);

  void accept(VisitorCheck visitor);

  Kind getKind();

  public enum Kind implements AstNodeType,GrammarRuleKey {

    /**
     * {@link CompilationUnitTree}
     */
    PROTO_UNIT(ProtoBufUnitTree.class),

    /**
     * {@link SyntaxTree}
     */
    SYNTAX(SyntaxTree.class),

    /**
     * {@link MessageTree}
     */
    MESSAGE(MessageTree.class),

    /**
     * {@link EnumTree}
     */
    ENUM(EnumTree.class),

    /**
     * {@link EnumValueTree}
     */
    ENUM_VALUE(EnumValueTree.class),

    /**
     * {@link FieldTree}
     */
    FIELD(FieldTree.class),

    /**
     * {@link FieldRuleTree}
     */
    FIELD_RULE(FieldRuleTree.class),

    /**
     * {@link FieldTypeTree}
     */
    FIELD_TYPE(FieldTypeTree.class),

    /**
     * {@link FieldScalarTypeTree}
     */
    FIELD_SCALAR_TYPE(FieldScalarTypeTree.class),

    /**
     * {@link IdentifierTree}
     */
    IDENTIFIER(IdentifierTree.class),

    /**
     * {@link SyntaxToken}
     */
    TOKEN(SyntaxToken.class),

    /**
     * {@link SyntaxToken}
     */
    TRIVIA(SyntaxTrivia.class);

    final Class<? extends Tree> associatedInterface;

    private Kind(Class<? extends Tree> associatedInterface) {
      this.associatedInterface = associatedInterface;
    }

    public Class<? extends Tree> getAssociatedInterface() {
      return associatedInterface;
    }
  }

}
