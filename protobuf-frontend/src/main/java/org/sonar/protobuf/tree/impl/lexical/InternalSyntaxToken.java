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
package org.sonar.protobuf.tree.impl.lexical;

import com.google.common.collect.Iterators;
import com.sonar.sslr.api.TokenType;
import java.util.Iterator;
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxTrivia;
import org.sonar.plugins.protobuf.api.visitors.VisitorCheck;
import org.sonar.protobuf.tree.impl.ProtoBufTree;

public class InternalSyntaxToken extends ProtoBufTree implements SyntaxToken {

  private final Kind KIND;

  private List<SyntaxTrivia> trivias;
  private int startIndex;
  private final int line;
  private final int column;
  private final String value;
  private final boolean isEOF;

  public InternalSyntaxToken(int line, int column, String value, List<SyntaxTrivia> trivias, int startIndex, boolean isEOF, TokenType type) {
    this.value = value;
    this.line = line;
    this.column = column;
    this.trivias = trivias;
    this.startIndex = startIndex;
    this.isEOF = isEOF;
    this.KIND = Kind.TOKEN;
  }

  public int toIndex() {
    return startIndex + value.length();
  }

  @Override
  public String text() {
    return value;
  }

  @Override
  public List<SyntaxTrivia> trivias() {
    return trivias;
  }

  @Override
  public int line() {
    return line;
  }

  @Override
  public int column() {
    return column;
  }

  public int startIndex() {
    return startIndex;
  }

  public boolean isEOF() {
    return isEOF;
  }

  public boolean is(TokenType type) {
    return this.text().equals(type.getValue());
  }

  @Override
  public Kind getKind() {
    return KIND;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.<Tree>concat(trivias().iterator());
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public void accept(VisitorCheck visitor) {
    visitor.visitToken(this);
  }

  @Override
  public SyntaxToken getFirstToken() {
    return this;
  }

  @Override
  public SyntaxToken getLastToken() {
    return this;
  }

  @Override
  public String toString() {
    return "[SyntaxToken] \"" + text() + "\"";
  }

}
