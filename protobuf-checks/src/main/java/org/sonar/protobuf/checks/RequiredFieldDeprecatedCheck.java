package org.sonar.protobuf.checks;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.protobuf.api.tree.FieldTree;
import org.sonar.plugins.protobuf.api.tree.SyntaxTree;
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
    if (StringUtils.equalsIgnoreCase("required", tree.rule().keyword().text())) {
      context().newIssue(RequiredFieldDeprecatedCheck.KEY, RequiredFieldDeprecatedCheck.MESSAGE).tree(tree);
    }
  }
}
