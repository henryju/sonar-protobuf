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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.protobuf.tree.impl.ProtoBufTree;

public abstract class ProtoBufTreeSubscriber {

  private Collection<Tree.Kind> nodesToVisit;

  public abstract List<Tree.Kind> nodesToVisit();

  public void visitNode(Tree tree) {
    // Default behavior : do nothing.
  }

  public void leaveNode(Tree tree) {
    // Default behavior : do nothing.
  }

  public void scanTree(Tree tree) {
    nodesToVisit = nodesToVisit();
    visit(tree);
  }

  private void visit(Tree tree) {
    boolean isSubscribed = isSubscribed(tree);
    if (isSubscribed) {
      visitNode(tree);
    }
    visitChildren(tree);
    if (isSubscribed) {
      leaveNode(tree);
    }
  }

  private boolean isSubscribed(Tree tree) {
    return nodesToVisit.contains(tree.getKind());
  }

  private void visitChildren(Tree tree) {
    ProtoBufTree protoTree = (ProtoBufTree) tree;

    if (!protoTree.isLeaf()) {
      for (Iterator<Tree> iter = protoTree.childrenIterator(); iter.hasNext();) {
        Tree next = iter.next();

        if (next != null) {
          visit(next);
        }
      }
    }
  }

}
