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

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.EnumTree;
import org.sonar.plugins.protobuf.api.tree.EnumValueTree;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.FieldTypeTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldRuleTree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldScalarTypeTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxTrivia;
import org.sonar.protobuf.tree.impl.ProtoBufTree;
import org.sonar.protobuf.tree.visitors.ProtoBufCheckContext;

public abstract class ProtoBufVisitorCheck implements VisitorCheck {

  private ProtoBufCheckContext context;

  @Override
  public void init() {
    // Default behavior : do nothing.
  }

  @Override
  public void visitToken(SyntaxToken token) {
    scan(token);
  }

  @Override
  public void visitTrivia(SyntaxTrivia trivia) {
    // Do nothing is the default behavior (There is no children to visit)
  }

  @Override
  public void visitProtoBufUnit(ProtoBufUnitTree tree) {
    scan(tree);
  }

  @Override
  public void visitField(FieldTree tree) {
    scan(tree);
  }

  @Override
  public void visitMessage(MessageTree tree) {
    scan(tree);
  }

  @Override
  public void visitIdentifier(IdentifierTree tree) {
    scan(tree);
  }

  @Override
  public void visitFieldType(FieldTypeTree tree) {
    scan(tree);
  }

  @Override
  public void visitFieldScalarType(FieldScalarTypeTree tree) {
    scan(tree);
  }

  @Override
  public void visitSyntax(SyntaxTree tree) {
    scan(tree);
  }

  @Override
  public void visitFieldRule(FieldRuleTree tree) {
    scan(tree);
  }

  @Override
  public void visitEnum(EnumTree tree) {
    scan(tree);
  }

  @Override
  public void visitEnumValue(EnumValueTree tree) {
    scan(tree);
  }

  @Override
  public CheckContext context() {
    return context;
  }

  protected void scan(Tree tree) {
    Iterator<Tree> childrenIterator = ((ProtoBufTree) tree).childrenIterator();
    Tree child;

    while (childrenIterator.hasNext()) {
      child = childrenIterator.next();
      if (child != null) {
        child.accept(this);
      }
    }
  }

  protected <T extends Tree> void scan(List<T> trees) {
    for (T tree : trees) {
      scan(tree);
    }
  }

  @Override
  public List<Issue> analyze(File file, ProtoBufUnitTree tree) {
    this.context = new ProtoBufCheckContext(file, tree);
    visitProtoBufUnit(tree);

    return context().getIssues();
  }
}
