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
import org.sonar.plugins.protobuf.api.tree.FieldTypeTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.ProtoBufTree;

public class FieldTypeTreeImpl extends ProtoBufTree implements FieldTypeTree {

  private final IdentifierTree identifier;

  public FieldTypeTreeImpl(IdentifierTree identifier) {
    this.identifier = identifier;
  }

  @Override
  public Kind getKind() {
    return Kind.FIELD_TYPE;
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitFieldType(this);
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>singletonIterator(identifier);
  }

  @Override
  public boolean isScalar() {
    return identifier.is(Kind.FIELD_SCALAR_TYPE);
  }

  @Override
  public String text() {
    return identifier.text();
  }

  @Override
  public IdentifierTree identifier() {
    return identifier;
  }

}
