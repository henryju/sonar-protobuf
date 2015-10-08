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
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.EnumTree;
import org.sonar.plugins.protobuf.api.tree.EnumValueTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class EnumTreeImpl extends ProtoBufTree implements EnumTree {

  private static final Kind KIND = Kind.ENUM;
  private final InternalSyntaxToken enumToken;
  private final IdentifierTree name;
  private final InternalSyntaxToken lcurlyToken;
  private final InternalSyntaxToken rcurlyToken;
  private final List<EnumValueTree> values;

  public EnumTreeImpl(InternalSyntaxToken enumToken, IdentifierTree name, InternalSyntaxToken lcurlyToken,
    List<EnumValueTree> values, InternalSyntaxToken rcurlyToken) {
    this.enumToken = enumToken;
    this.name = name;
    this.lcurlyToken = lcurlyToken;
    this.values = values;
    this.rcurlyToken = rcurlyToken;
  }

  @Override
  public String name() {
    return this.name.text();
  }

  @Override
  public IdentifierTree identifier() {
    return this.name;
  }

  @Override
  public List<EnumValueTree> values() {
    return values;
  }

  @Override
  public Kind getKind() {
    return KIND;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(Iterators.<Tree>forArray(enumToken, name, lcurlyToken),
      values.iterator(),
      Iterators.singletonIterator(rcurlyToken));
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitEnum(this);
  }
}
