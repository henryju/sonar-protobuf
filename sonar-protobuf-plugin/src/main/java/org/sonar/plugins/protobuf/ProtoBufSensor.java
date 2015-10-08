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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.protobuf.api.ProtoBuf;
import org.sonar.plugins.protobuf.api.visitors.ProtoBufCheck;
import org.sonar.plugins.protobuf.api.visitors.SymbolHighlighterVisitor;
import org.sonar.plugins.protobuf.api.visitors.SyntaxHighlighterVisitor;
import org.sonar.protobuf.ProtoBufAnalyzer;
import org.sonar.protobuf.checks.CheckList;
import org.sonar.squidbridge.api.CodeVisitor;

public class ProtoBufSensor implements Sensor {

  private final ResourcePerspectives resourcePerspectives;
  private final FileSystem fileSystem;
  private final FilePredicate mainFilePredicate;
  private final Checks<CodeVisitor> checks;

  public ProtoBufSensor(ResourcePerspectives resourcePerspectives, FileSystem filesystem,
    CheckFactory checkFactory) {
    this.checks = checkFactory
      .<CodeVisitor>create(CheckList.REPOSITORY_KEY)
      .addAnnotatedChecks(CheckList.getChecks());
    this.resourcePerspectives = resourcePerspectives;
    this.fileSystem = filesystem;
    this.mainFilePredicate = fileSystem.predicates().and(
      fileSystem.predicates().hasType(InputFile.Type.MAIN),
      fileSystem.predicates().hasLanguage(ProtoBuf.KEY));
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return fileSystem.hasFiles(mainFilePredicate);
  }

  private List<CodeVisitor> getCheckVisitors() {
    return new ArrayList<>(checks.all());
  }

  @Override
  public void analyse(Project module, SensorContext context) {
    List<CodeVisitor> visitors = getCheckVisitors();

    ImmutableList.Builder<ProtoBufCheck> protobufCheckBuilder = ImmutableList.builder();

    for (CodeVisitor codeVisitor : visitors) {
      if (codeVisitor instanceof ProtoBufCheck) {
        protobufCheckBuilder.add((ProtoBufCheck) codeVisitor);
      }
    }

    protobufCheckBuilder.add(new SyntaxHighlighterVisitor(resourcePerspectives, fileSystem));
    protobufCheckBuilder.add(new SymbolHighlighterVisitor(resourcePerspectives, fileSystem));

    ProtoBufAnalyzer analyzer = new ProtoBufAnalyzer(fileSystem.encoding(), protobufCheckBuilder.build());
    ArrayList<InputFile> inputFiles = Lists.newArrayList(fileSystem.inputFiles(mainFilePredicate));

    for (InputFile inputFile : inputFiles) {
      saveIssues(analyzer.analyze(inputFile.file()), inputFile);
    }

  }

  private void saveIssues(List<org.sonar.plugins.protobuf.api.visitors.Issue> issues, InputFile inputFile) {
    for (org.sonar.plugins.protobuf.api.visitors.Issue issue : issues) {
      RuleKey ruleKey = RuleKey.of(CheckList.REPOSITORY_KEY, issue.ruleKey());
      Issuable issuable = resourcePerspectives.as(Issuable.class, inputFile);

      if (issuable != null) {
        Issuable.IssueBuilder issueBuilder = issuable.newIssueBuilder()
          .ruleKey(ruleKey)
          .message(issue.message())
          .effortToFix(issue.cost());

        if (issue.line() > 0) {
          issueBuilder.line(issue.line());
        }

        issuable.addIssue(issueBuilder.build());
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
