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

import java.util.List;

import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.plugins.protobuf.api.ProtoBuf;

import com.google.common.collect.ImmutableList;

public class ProtoBufPlugin extends SonarPlugin {

  public static final String FILE_SUFFIXES_KEY = "sonar.proto.file.suffixes";

  public static final String PROTO_CATEGORY = "Protocol Buffers";
  public static final String GENERAL_SUBCATEGORY = "General";

  /**
   * Gets the extensions.
   * 
   * @return the extensions
   * @see org.sonar.api.SonarPlugin#getExtensions()
   */
  @Override
  public List getExtensions() {
    return ImmutableList.of(

      ProtoBuf.class,

      ProtoBufSensor.class,

      ProtoBufRulesDefinition.class,
      ProtoBufProfile.class,

      // Properties
      PropertyDefinition.builder(FILE_SUFFIXES_KEY)
        .defaultValue(ProtoBuf.DEFAULT_FILE_SUFFIXES)
        .name("File Suffixes")
        .description("Comma-separated list of suffixes of Protocol Buffers files to analyze.")
        .onQualifiers(Qualifiers.MODULE, Qualifiers.PROJECT)
        .category(PROTO_CATEGORY)
        .subCategory(GENERAL_SUBCATEGORY)
        .build()

      );
  }
}
