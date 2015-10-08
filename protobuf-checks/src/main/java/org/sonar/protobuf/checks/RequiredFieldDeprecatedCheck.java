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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.visitors.ProtoBufVisitorCheck;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.annotations.Tags;

@Rule(
  key = RequiredFieldDeprecatedCheck.KEY,
  name = "required field is no longer authorized in Proto3 and not recommended in Proto2",
  priority = Priority.MAJOR,
  tags = {Tags.OBSOLETE})
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
public class RequiredFieldDeprecatedCheck extends ProtoBufVisitorCheck {

  public static final String KEY = "PB1003";
  private static final String MESSAGE = "\"required\" rule should not be used in Proto2 and is not authorized in Proto3";

  @Override
  public void visitField(FieldTree tree) {
    if (tree.rule() != null && StringUtils.equalsIgnoreCase("required", tree.rule().keyword().text())) {
      context().newIssue(RequiredFieldDeprecatedCheck.KEY, RequiredFieldDeprecatedCheck.MESSAGE).tree(tree);
    }
  }
}
