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
package org.sonar.protobuf;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.typed.ActionParser;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import org.sonar.plugins.protobuf.api.tree.ProtoBufUnitTree;
import org.sonar.plugins.protobuf.api.tree.Tree;
import org.sonar.plugins.protobuf.api.visitors.Issue;
import org.sonar.plugins.protobuf.api.visitors.ProtoBufCheck;
import org.sonar.protobuf.parser.ProtoBufParserBuilder;

public class ProtoBufAnalyzer {

  private final ActionParser<Tree> parser;
  private final ImmutableList<ProtoBufCheck> checks;

  public ProtoBufAnalyzer(Charset charset, ImmutableList<ProtoBufCheck> checks) {
    this.parser = ProtoBufParserBuilder.createParser(charset);
    this.checks = checks;

    for (ProtoBufCheck check : checks) {
      check.init();
    }
  }

  public List<Issue> analyze(File file) {
    // fixme : handle parsing exceptions
    ProtoBufUnitTree tree = (ProtoBufUnitTree) parser.parse(file);

    ImmutableList.Builder<Issue> issuesBuilder = ImmutableList.builder();
    for (ProtoBufCheck check : checks) {
      issuesBuilder.addAll(check.analyze(file, tree));
    }

    return issuesBuilder.build();
  }

}
