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
package org.sonar.plugins.protobuf;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.protobuf.api.ProtoBuf;
import org.sonar.protobuf.checks.CheckList;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

public class ProtoBufRulesDefinition implements RulesDefinition {

  private static final String REPOSITORY_NAME = "SonarQube";

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(CheckList.REPOSITORY_KEY, ProtoBuf.KEY).setName(REPOSITORY_NAME);
    AnnotationBasedRulesDefinition.load(repository, ProtoBuf.KEY, CheckList.getChecks());
    repository.done();
  }

}
