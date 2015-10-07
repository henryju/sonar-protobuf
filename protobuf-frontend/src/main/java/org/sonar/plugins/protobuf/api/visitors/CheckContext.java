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

@Beta
public interface CheckContext {

  /**
   * @return the top tree node of the current file AST representation.
   */
  ProtoBufUnitTree tree();

  /**
   *
   * <p> To set line and cost use {@link Issue#line(int)} and {@link Issue#cost(double)}. Note, that these calls could be chained.
   * <pre>
   *   newIssue("S000", "Some message")
   *     .line(105)
   *     .cost(3);
   * </pre>
   *
   * @param ruleKey key of the rule for which issue should be created
   * @param message message of the issue
   * @return instance of Issue
   */
  Issue newIssue(String ruleKey, String message);

  /**
   * @return the current file
   */
  File file();

  List<Issue> getIssues();

}
