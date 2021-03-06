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
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class SyntaxTreeImpl extends ProtoBufTree implements SyntaxTree {

  private static final Kind KIND = Kind.SYNTAX;
  private final InternalSyntaxToken token;
  private final InternalSyntaxToken eq;
  private final InternalSyntaxToken value;
  private final InternalSyntaxToken colon;

  public SyntaxTreeImpl(InternalSyntaxToken token, InternalSyntaxToken eq, InternalSyntaxToken value, InternalSyntaxToken colon) {
    this.token = token;
    this.eq = eq;
    this.value = value;
    this.colon = colon;
  }

  @Override
  public String syntax() {
    return this.value.text().substring(1, this.value.text().length() - 1);
  }

  @Override
  public Kind getKind() {
    return KIND;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>forArray(token, eq, value, colon);
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitSyntax(this);
  }
}
