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
package org.sonar.protobuf.tree.impl;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import javax.annotation.Nullable;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.FieldTypeTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldRuleTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class FieldTreeImpl extends ProtoBufTree implements FieldTree {

  private static final Kind KIND = Kind.FIELD;
  private final FieldTypeTree type;
  private final IdentifierTree identifier;
  private final InternalSyntaxToken eq;
  private final InternalSyntaxToken tag;
  private final InternalSyntaxToken colon;
  private final FieldRuleTree rule;

  public FieldTreeImpl(@Nullable FieldRuleTree rule, FieldTypeTree type, IdentifierTree identifier, InternalSyntaxToken eq, InternalSyntaxToken tag,
    InternalSyntaxToken colon) {
    this.rule = rule;
    this.type = type;
    this.identifier = identifier;
    this.eq = eq;
    this.tag = tag;
    this.colon = colon;
  }

  @Override
  public FieldTypeTree type() {
    return type;
  }

  @Override
  public String name() {
    return this.identifier.text();
  }

  @Override
  public FieldRuleTree rule() {
    return rule;
  }

  @Override
  public boolean isScalar() {
    return type.isScalar();
  }

  @Override
  public Kind getKind() {
    return KIND;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>forArray(rule, type, identifier, eq, tag, colon);
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitField(this);
  }

}
