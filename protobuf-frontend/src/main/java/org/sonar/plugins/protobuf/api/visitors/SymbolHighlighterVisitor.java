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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.source.Symbol;
import org.sonar.api.source.Symbolizable;
import org.sonar.api.source.Symbolizable.SymbolTableBuilder;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.MessageTree;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.lexical.SyntaxToken;

public class SymbolHighlighterVisitor extends AbstractHighlighterVisitor {

  private SymbolTableBuilder symbolTableBuilder;
  private Stack<String> msgStack = new Stack<>();
  private Map<String, Symbol> messageDefs = new HashMap<>();
  private Multimap<String, SyntaxToken> referencesByFqn = HashMultimap.create();

  public SymbolHighlighterVisitor(ResourcePerspectives resourcePerspectives, FileSystem fs) {
    super(resourcePerspectives, fs);
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
    lineStart = SyntaxHighlighterVisitor.startLines(file, this.charset);
    messageDefs.clear();
    referencesByFqn.clear();
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

}
