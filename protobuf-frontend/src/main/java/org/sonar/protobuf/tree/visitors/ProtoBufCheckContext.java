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
package org.sonar.protobuf.tree.visitors;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.sonar.plugins.protobuf.api.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.visitors.CheckContext;
import org.sonar.plugins.protobuf.api.visitors.Issue;

public class ProtoBufCheckContext implements CheckContext {

  private final File file;
  private final ProtoBufUnitTree tree;
  private List<Issue> issues;

  public ProtoBufCheckContext(File file, ProtoBufUnitTree tree) {
    this.file = file;
    this.tree = tree;
    this.issues = new ArrayList<>();
  }

  @Override
  public ProtoBufUnitTree tree() {
    return tree;
  }

  @Override
  public ProtoBufIssue newIssue(String ruleKey, String message) {
    ProtoBufIssue issue = new ProtoBufIssue(ruleKey, message);
    issues.add(issue);

    return issue;
  }

  @Override
  public File file() {
    return file;
  }

  @Override
  public ImmutableList<Issue> getIssues() {
    return ImmutableList.copyOf(issues);
  }

}
