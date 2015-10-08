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

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.sonar.plugins.protobuf.CheckTest;
import org.sonar.plugins.protobuf.TestUtils;
import org.sonar.plugins.protobuf.api.tests.ProtoBufCheckTest;
import org.sonar.plugins.protobuf.api.visitors.Issue;
import org.sonar.protobuf.tree.visitors.ProtoBufIssue;

public class MessageNameCustomFormatCheckTest extends CheckTest {

  private MessageNameCheck check = new MessageNameCheck();
  private String fileName = "MessageNameCustomFormatCheck.proto";

  @Test
  public void customFormat() throws Exception {
    check.format = "^[A-Z]*$";
    List<Issue> expectedIssues = new LinkedList<>();
    expectedIssues.add(new ProtoBufIssue("test", "Rename Message \"badname\" to match the regular expression " + check.format + ".").line(1));
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile(fileName), expectedIssues);
  }
}
