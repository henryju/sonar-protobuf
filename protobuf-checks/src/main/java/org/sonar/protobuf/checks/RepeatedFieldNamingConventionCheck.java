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
  key = RepeatedFieldNamingConventionCheck.KEY,
  name = "Repeated field name should not finish with a 's'",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION})
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("5min")
public class RepeatedFieldNamingConventionCheck extends ProtoBufVisitorCheck {

  public static final String KEY = "PB1004";
  private static final String MESSAGE = "Remove the 's' at the end of \"%s\"";

  @Override
  public void visitField(FieldTree tree) {
    if (tree.rule() != null
      && StringUtils.equalsIgnoreCase("repeated", tree.rule().keyword().text())
      && tree.name().endsWith("s")) {
      String message = String.format(MESSAGE, tree.name());
      context().newIssue(RepeatedFieldNamingConventionCheck.KEY, message).tree(tree);
    }
  }

}
