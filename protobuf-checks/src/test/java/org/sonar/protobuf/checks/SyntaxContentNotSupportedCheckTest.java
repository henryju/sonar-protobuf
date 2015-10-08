package org.sonar.protobuf.checks;

import org.junit.Test;
import org.sonar.plugins.protobuf.TestUtils;
import org.sonar.plugins.protobuf.api.tests.ProtoBufCheckTest;

public class SyntaxContentNotSupportedCheckTest {

  private SyntaxContentNotSupportedCheck check = new SyntaxContentNotSupportedCheck();

  @Test
  public void syntaxIsContainingSupportedVersion() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("SyntaxContentNotSupportedOKCheck.proto"));
  }

  @Test
  public void syntaxIsNotContainingSupportedVersion() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("SyntaxContentNotSupportedKOCheck.proto"));
  }

}
