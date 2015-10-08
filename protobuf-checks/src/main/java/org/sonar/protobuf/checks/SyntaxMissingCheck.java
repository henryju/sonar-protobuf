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

import java.util.List;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.visitors.ProtoBufSubscriptionCheck;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.annotations.Tags;

import com.google.common.collect.ImmutableList;

@Rule(
  key = SyntaxMissingCheck.KEY,
  name = "Version of Protocol Buffers thru the \"syntax\" keyword should be given",
  priority = Priority.MINOR,
  tags = {Tags.USER_EXPERIENCE})
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
public class SyntaxMissingCheck extends ProtoBufSubscriptionCheck {

  public static final String KEY = "PB1001";
  private static final String MESSAGE = "Set the version of your Protocol Buffers file using the keyword \"syntax\"";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.PROTO_UNIT);
  }

  @Override
  public void visitNode(Tree tree) {
    ProtoBufUnitTree protoUnitTree = (ProtoBufUnitTree) tree;
    if (protoUnitTree.syntax() == null) {
      context().newIssue(SyntaxMissingCheck.KEY, SyntaxMissingCheck.MESSAGE).tree(tree);
    }

  }

}
