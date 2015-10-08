package org.sonar.protobuf.checks;

import org.junit.Test;
import org.sonar.plugins.protobuf.TestUtils;
import org.sonar.plugins.protobuf.api.tests.ProtoBufCheckTest;

public class RequiredFieldDeprecatedCheckTest {

  private RequiredFieldDeprecatedCheck check = new RequiredFieldDeprecatedCheck();

  @Test
  public void requiredIsNotUsed() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("RequiredFieldDeprecatedOKCheck.proto"));
  }

  @Test
  public void requiredMustNotBeUsed() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("RequiredFieldDeprecatedKOCheck.proto"));
  }

}
