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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.lexical.InternalSyntaxToken;

public class MessageTreeImpl extends ProtoBufTree implements MessageTree {

  private static final Kind KIND = Kind.MESSAGE;
  private final InternalSyntaxToken messageToken;
  private final IdentifierTree name;
  private final InternalSyntaxToken lcurlyToken;
  private final InternalSyntaxToken rcurlyToken;
  private final List<FieldTree> fields;
  private final List<MessageTree> messages;
  private final List<Tree> fieldsOrSubMessages;

  public MessageTreeImpl(InternalSyntaxToken messageToken, IdentifierTree name, InternalSyntaxToken lcurlyToken,
    List<Tree> fieldsOrSubMessages, InternalSyntaxToken rcurlyToken) {
    this.messageToken = messageToken;
    this.name = name;
    this.lcurlyToken = lcurlyToken;
    this.fieldsOrSubMessages = fieldsOrSubMessages;
    this.fields = new ArrayList<>();
    this.messages = new ArrayList<>();
    for (Tree tree : fieldsOrSubMessages) {
      if (tree instanceof FieldTree) {
        fields.add((FieldTree) tree);
      } else if (tree instanceof MessageTree) {
        messages.add((MessageTree) tree);
      } else {
        throw new IllegalArgumentException("Unsupported nested type for message: " + tree.getKind());
      }
    }
    this.rcurlyToken = rcurlyToken;
  }

  @Override
  public String name() {
    return this.name.text();
  }

  @Override
  public List<FieldTree> fields() {
    return fields;
  }

  @Override
  public Kind getKind() {
    return KIND;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.concat(Iterators.<Tree>forArray(messageToken, name, lcurlyToken),
      fieldsOrSubMessages.iterator(),
      Iterators.singletonIterator(rcurlyToken));
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitMessage(this);
  }
}
