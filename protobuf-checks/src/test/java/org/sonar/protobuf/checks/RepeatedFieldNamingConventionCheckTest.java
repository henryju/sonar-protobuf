package org.sonar.protobuf.checks;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sonar.plugins.protobuf.TestUtils;
import org.sonar.plugins.protobuf.api.tests.ProtoBufCheckTest;

public class RepeatedFieldNamingConventionCheckTest {

  private RepeatedFieldNamingConventionCheck check = new RepeatedFieldNamingConventionCheck();

  @Test
  public void requiredWithoutPlural() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("RepeatedFieldNamingConventionOKCheck.proto"));
  }

  @Test
  public void requiredWithPlural() throws Exception {
    ProtoBufCheckTest.check(check, TestUtils.getCheckFile("RepeatedFieldNamingConventionKOCheck.proto"));
  }

}
