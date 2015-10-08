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
package org.sonar.protobuf.checks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.visitors.ProtoBufVisitorCheck;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.annotations.Tags;

import com.google.common.collect.ImmutableList;

@Rule(
  key = SyntaxContentNotSupportedCheck.KEY,
  name = "Protocol Buffers is supporting only 2 versions: proto2 and proto3",
  priority = Priority.MAJOR,
  tags = {Tags.USER_EXPERIENCE})
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
public class SyntaxContentNotSupportedCheck extends ProtoBufVisitorCheck {

  public static final String KEY = "PB1002";
  private static final String MESSAGE = "\"syntax\" keyword can contain only 2 possible values : proto2 or proto3";

  private final Set<String> supportedProtoBufVersions = new HashSet<String>();

  @Override
  public void init() {
    super.init();

    supportedProtoBufVersions.add("proto2");
    supportedProtoBufVersions.add("proto3");
  }

  @Override
  public void visitSyntax(SyntaxTree tree) {
    if (!supportedProtoBufVersions.contains(tree.syntax())) {
      context().newIssue(SyntaxContentNotSupportedCheck.KEY, SyntaxContentNotSupportedCheck.MESSAGE).tree(tree);
    }
  }

}
