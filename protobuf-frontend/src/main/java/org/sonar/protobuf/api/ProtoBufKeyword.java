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
package org.sonar.protobuf.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;

public enum ProtoBufKeyword implements TokenType,GrammarRuleKey {

  SYNTAX("syntax"),
  MESSAGE("message"),
  ENUM("enum"),
  SERVICE("service"),

  REQUIRED("required"),
  OPTIONAL("optional"),
  REPEATED("repeated"),

  DOUBLE("double"),
  FLOAT("float"),
  INT32("int32"),
  INT64("int64"),
  UINT32("uint32"),
  UINT64("uint64"),
  SINT32("sint32"),
  SINT64("sint64"),
  FIXED32("fixed32"),
  FIXED64("fixed64"),
  SFIXED32("sfixed32"),
  SFIXED64("sfixed64"),
  BOOL("bool"),
  STRING("string"),
  BYTES("bytes");

  private final String value;

  private ProtoBufKeyword(String value) {
    this.value = value;
  }

  @Override
  public boolean hasToBeSkippedFromAst(AstNode astNode) {
    return false;
  }

  @Override
  public String getName() {
    return name();
  }

  @Override
  public String getValue() {
    return value;
  }

  public static String[] getKeywordValues() {
    ProtoBufKeyword[] keywordsEnum = ProtoBufKeyword.values();
    String[] keywords = new String[keywordsEnum.length];
    for (int i = 0; i < keywords.length; i++) {
      keywords[i] = keywordsEnum[i].getValue();
    }
    return keywords;
  }

}
