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
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class ProtoBufUnitTreeImpl extends ProtoBufTree implements ProtoBufUnitTree {

  private static final Kind KIND = Kind.PROTO_UNIT;

  private final InternalSyntaxToken eofToken;
  private final List<MessageTree> messages;
  private final SyntaxTree syntaxTree;

  public ProtoBufUnitTreeImpl(@Nullable SyntaxTree syntaxTree, List<MessageTree> messages, InternalSyntaxToken eofToken) {
    this.syntaxTree = syntaxTree;
    this.messages = messages;
    this.eofToken = eofToken;
  }

  public List<MessageTree> messages() {
    return messages;
  }

  @CheckForNull
  @Override
  public SyntaxTree syntax() {
    return syntaxTree;
  }

  @Override
  public SyntaxToken eofToken() {
    return eofToken;
  }

  @Override
  public Kind getKind() {
    return KIND;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(
      Iterators.singletonIterator(syntaxTree),
      messages.iterator(),
      Iterators.singletonIterator(eofToken));
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitProtoBufUnit(this);
  }
}
