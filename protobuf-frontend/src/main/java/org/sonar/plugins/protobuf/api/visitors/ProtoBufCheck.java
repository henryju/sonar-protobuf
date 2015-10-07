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

import com.google.common.annotations.Beta;
import java.io.File;
import java.util.List;
import org.sonar.plugins.protobuf.api.ProtoBufUnitTree;
import org.sonar.squidbridge.api.CodeVisitor;

/**
 * Marker interface for all ProtoBuf checks.
 *
 * To implement a check you should extend {@link ProtoBufVisitorCheck} or {@link ProtoBufSubscriptionCheck}.
 */
@Beta
public interface ProtoBufCheck extends CodeVisitor {

  CheckContext context();

  /**
   * Initialize the check, this method is called once.
   */
  void init();

  List<Issue> analyze(File file, ProtoBufUnitTree tree);
}
