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

import javax.annotation.Nullable;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.visitors.Issue;
import org.sonar.protobuf.tree.impl.ProtoBufTree;

/**
 * This class is used to represent issue created by checks before feeding them to SonarQube.
 */
public class ProtoBufIssue implements Issue {

  private final String ruleKey;
  private final String message;
  private int line;
  @Nullable
  private Double cost;

  public ProtoBufIssue(String ruleKey, String message) {
    this.ruleKey = ruleKey;
    this.message = message;
    this.line = 0;
    this.cost = null;
  }

  @Override
  public String ruleKey() {
    return ruleKey;
  }

  @Override
  public int line() {
    return line;
  }

  @Override
  @Nullable
  public Double cost() {
    return cost;
  }

  @Override
  public String message() {
    return message;
  }

  @Override
  public ProtoBufIssue line(int line) {
    this.line = line;
    return this;
  }

  @Override
  public ProtoBufIssue tree(Tree tree) {
    this.line = ((ProtoBufTree) tree).getLine();
    return this;
  }

  @Override
  public ProtoBufIssue cost(double cost) {
    this.cost = cost;
    return this;
  }

}
