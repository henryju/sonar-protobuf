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
package org.sonar.plugins.protobuf.api.visitors;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.source.Symbol;
import org.sonar.api.source.Symbolizable;
import org.sonar.api.source.Symbolizable.SymbolTableBuilder;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;

public class SymbolHighlighterVisitor extends ProtoBufVisitorCheck {

  private SymbolTableBuilder symbolTableBuilder;

  private final ResourcePerspectives resourcePerspectives;
  private final FileSystem fs;
  private final Charset charset;

  private List<Integer> lineStart;

  private Stack<String> msgStack = new Stack<>();

  private Map<String, Symbol> messageDefs = new HashMap<>();
  private Multimap<String, SyntaxToken> referencesByFqn = HashMultimap.create();

  public SymbolHighlighterVisitor(ResourcePerspectives resourcePerspectives, FileSystem fs) {
    this.resourcePerspectives = resourcePerspectives;
    this.fs = fs;
    this.charset = fs.encoding();
  }

  @Override
  public void visitMessage(MessageTree tree) {
    String message = tree.name();
    msgStack.push(message);
    messageDefs.put(fqn(), symbolTableBuilder.newSymbol(start(tree.identifier().token()), end(tree.identifier().token())));
    super.visitMessage(tree);
    msgStack.pop();
  }

  @Override
  public void visitField(FieldTree tree) {
    if (!tree.isScalar()) {
      referencesByFqn.put(tree.type().text(), tree.type().identifier().token());
    }
    super.visitField(tree);
  }

  private String fqn() {
    return Joiner.on('.').join(msgStack);
  }

  @Override
  public List<Issue> analyze(File file, ProtoBufUnitTree tree) {
    Symbolizable symbolizable = resourcePerspectives.as(Symbolizable.class, inputFromIOFile(file));
    symbolTableBuilder = symbolizable.newSymbolTableBuilder();
    lineStart = startLines(file, this.charset);

    List<Issue> issues = super.analyze(file, tree);

    for (String fqn : referencesByFqn.keySet()) {
      Symbol def = messageDefs.get(fqn);
      if (def != null) {
        for (SyntaxToken ref : referencesByFqn.get(fqn)) {
          symbolTableBuilder.newReference(def, start(ref));
        }
      }
    }

    symbolizable.setSymbolTable(symbolTableBuilder.build());
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
