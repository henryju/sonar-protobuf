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

import java.io.File;
import java.util.List;
import org.sonar.plugins.protobuf.api.ProtoBufUnitTree;
import org.sonar.protobuf.tree.visitors.ProtoBufCheckContext;

public abstract class ProtoBufSubscriptionCheck extends ProtoBufTreeSubscriber implements ProtoBufCheck {

  private CheckContext context;

  @Override
  public CheckContext context() {
    return context;
  }

  @Override
  public void init() {
    // Default behavior : do nothing.
  }

  @Override
  public final List<Issue> analyze(File file, ProtoBufUnitTree tree) {
    this.context = new ProtoBufCheckContext(file, tree);
    scanTree(context.tree());

    return context().getIssues();
  }

}
