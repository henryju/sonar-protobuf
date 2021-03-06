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
package org.sonar.protobuf.tree.impl.expression;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import org.sonar.plugins.protobuf.api.tree.EnumValueTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.ProtoBufTree;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class EnumValueTreeImpl extends ProtoBufTree implements EnumValueTree {

  private final IdentifierTree identifier;
  private final InternalSyntaxToken eq;
  private final InternalSyntaxToken tag;
  private final InternalSyntaxToken colon;

  public EnumValueTreeImpl(IdentifierTree identifier, InternalSyntaxToken eq, InternalSyntaxToken tag,
    InternalSyntaxToken colon) {
    this.identifier = identifier;
    this.eq = eq;
    this.tag = tag;
    this.colon = colon;
  }

  @Override
  public Kind getKind() {
    return Kind.ENUM_VALUE;
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitEnumValue(this);
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>forArray(identifier, eq, tag, colon);
  }

  @Override
  public String name() {
    return identifier.text();
  }

}
