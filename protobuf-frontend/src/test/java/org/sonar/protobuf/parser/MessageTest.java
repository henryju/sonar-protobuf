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
package org.sonar.protobuf.parser;

import com.sonar.sslr.api.Grammar;
import org.junit.Test;
import org.sonar.protobuf.utils.Assertions;

public class MessageTest {

  Grammar g = ProtoBufLexicalGrammar.createGrammarBuilder().build();

  @Test
  public void ok() {
    Assertions.assertThat(ProtoBufLexicalGrammar.MESSAGE)
      .matches("message Foo { \n }")
      .matches("# Some comment\nmessage Foo { \n }")
      .matches("message Foo { }")
      .matches("message Foo {\n message Bar {} }")
      .matches("message Foo {\n enum Bar {} }")
      .matches("message Foo { int32 foo =1; }")
      .matches("message Foo { int32 foo =1; }");
  }

}
