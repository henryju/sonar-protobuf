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
    if (StringUtils.equalsIgnoreCase("repeated", tree.rule().keyword().text())) {
      if (tree.name().endsWith("s")) {
        String message = String.format(MESSAGE, tree.name());
        context().newIssue(RepeatedFieldNamingConventionCheck.KEY, message).tree(tree);
      }
    }
  }

}
