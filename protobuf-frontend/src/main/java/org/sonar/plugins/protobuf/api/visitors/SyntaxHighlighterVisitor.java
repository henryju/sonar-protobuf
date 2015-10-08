/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.source.Highlightable;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.expression.IdentifierTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;
import org.sonar.protobuf.api.ProtoBufKeyword;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class SyntaxHighlighterVisitor extends ProtoBufVisitorCheck {

  private Highlightable.HighlightingBuilder highlighting;
  private final Set<String> keywords;

  private final ResourcePerspectives resourcePerspectives;
  private final FileSystem fs;
  private final Charset charset;

  private List<Integer> lineStart;

  public SyntaxHighlighterVisitor(ResourcePerspectives resourcePerspectives, FileSystem fs, Charset charset) {
    this.resourcePerspectives = resourcePerspectives;
    this.fs = fs;
    this.charset = charset;

    ImmutableSet.Builder<String> keywordsBuilder = ImmutableSet.builder();
    keywordsBuilder.add(ProtoBufKeyword.getKeywordValues());
    keywords = keywordsBuilder.build();
  }

  @Override
  public void visitToken(SyntaxToken token) {
    String text = token.text();
    if (keywords.contains(text)) {
      highlighting.highlight(start(token), end(token), "k");
    }
    scan(token);
  }

  @Override
  public void visitMessage(MessageTree message) {

  }

  @Override
  public void visitIdentifier(IdentifierTree identifier) {

  }

  @Override
  public List<Issue> analyze(File file, ProtoBufUnitTree tree) {
    Highlightable highlightable = resourcePerspectives.as(Highlightable.class, inputFromIOFile(file));
    highlighting = highlightable.newHighlighting();
    lineStart = startLines(file, this.charset);

    List<Issue> issues = super.analyze(file, tree);

    highlighting.done();
    return issues;
  }

  private InputFile inputFromIOFile(File file) {
    return fs.inputFile(fs.predicates().is(file));
  }

  private int start(SyntaxToken token) {
    return getOffset(token.line(), token.column());
  }

  private int end(SyntaxToken token) {
    return getOffset(token.line(), token.column()) + token.text().length();
  }

  /**
   * @param line starts from 1
   * @param column starts from 0
   */
  private int getOffset(int line, int column) {
    return lineStart.get(line - 1) + column;
  }

  private static List<Integer> startLines(File file, Charset charset) {
    List<Integer> startLines = Lists.newArrayList();
    final String content;
    try {
      content = Files.toString(file, charset);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
    startLines.add(0);
    for (int i = 0; i < content.length(); i++) {
      if (content.charAt(i) == '\n' || (content.charAt(i) == '\r' && i + 1 < content.length() && content.charAt(i + 1) != '\n')) {
        startLines.add(i + 1);
      }
    }
    return startLines;
  }

}
