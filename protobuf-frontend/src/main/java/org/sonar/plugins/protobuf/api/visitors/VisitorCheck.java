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
package org.sonar.plugins.protobuf.api.visitors;

import com.google.common.annotations.Beta;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.FieldTypeTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldRuleTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldScalarTypeTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxTrivia;

@Beta
public interface VisitorCheck extends ProtoBufCheck {

  void visitToken(SyntaxToken token);

  void visitTrivia(SyntaxTrivia trivia);

  void visitProtoBufUnit(ProtoBufUnitTree tree);

  void visitMessage(MessageTree message);

  void visitIdentifier(IdentifierTree identifier);

  void visitSyntax(SyntaxTree syntaxTree);

  void visitFieldScalarType(FieldScalarTypeTree fieldScalar);

  void visitField(FieldTree fieldTree);

  void visitFieldRule(FieldRuleTree fieldRule);

  void visitFieldType(FieldTypeTree fieldTypeTree);
}
