package org.sonar.protobuf.checks;

import org.junit.Test;
import org.sonar.plugins.protobuf.TestUtils;
import org.sonar.plugins.protobuf.api.tests.ProtoBufCheckTest;

public class FieldMustNotBeCamelCaseCheckTest {

  private FieldMustNotBeCamelCaseCheck check = new FieldMustNotBeCamelCaseCheck();

  @Test
  public void correctFieldName() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("FieldMustNotBeCamelCaseOKCheck.proto"));
  }

  @Test
  public void notAcceptedFieldName() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("FieldMustNotBeCamelCaseKOCheck.proto"));
  }

}
