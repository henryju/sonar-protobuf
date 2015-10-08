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
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.FieldScalarTypeTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.ProtoBufTree;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class FieldScalarTypeTreeImpl extends ProtoBufTree implements FieldScalarTypeTree {

  private final InternalSyntaxToken token;

  public FieldScalarTypeTreeImpl(InternalSyntaxToken token) {
    this.token = token;
  }

  @Override
  public Kind getKind() {
    return Kind.FIELD_SCALAR_TYPE;
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitFieldScalarType(this);
  }

  @Override
  public SyntaxToken token() {
    return token;
  }

  @Override
  public String text() {
    return token.text();
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>singletonIterator(token);
  }

}
