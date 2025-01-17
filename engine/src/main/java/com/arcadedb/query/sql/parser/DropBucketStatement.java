/*
 * Copyright © 2021-present Arcade Data Ltd (info@arcadedata.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-FileCopyrightText: 2021-present Arcade Data Ltd (info@arcadedata.com)
 * SPDX-License-Identifier: Apache-2.0
 */
/* Generated By:JJTree: Do not edit this line. ODropClusterStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_USERTYPE_VISIBILITY_PUBLIC=true */
package com.arcadedb.query.sql.parser;

import com.arcadedb.database.Database;
import com.arcadedb.database.DatabaseInternal;
import com.arcadedb.exception.CommandExecutionException;
import com.arcadedb.exception.SchemaException;
import com.arcadedb.query.sql.executor.CommandContext;
import com.arcadedb.query.sql.executor.InternalResultSet;
import com.arcadedb.query.sql.executor.ResultInternal;
import com.arcadedb.query.sql.executor.ResultSet;
import com.arcadedb.schema.DocumentType;

import java.util.*;

public class DropBucketStatement extends DDLStatement {
  protected Identifier name;
  protected PInteger   id;
  protected boolean    ifExists = false;

  public DropBucketStatement(final int id) {
    super(id);
  }

  @Override
  public ResultSet executeDDL(final CommandContext context) {
    final DatabaseInternal database = context.getDatabase();

    // CHECK EXISTANCE AND RETRIEVE BUCKET NAME
    String bucketName = null;
    if (id != null) {
      try {
        bucketName = database.getSchema().getBucketById(id.getValue().intValue()).getName();
      } catch (SchemaException e) {
        if (ifExists)
          return new InternalResultSet();
        throw new CommandExecutionException("Bucket '" + name + "' not found");
      }
    } else {
      if (ifExists && !database.getSchema().existsBucket(name.getStringValue()))
        return new InternalResultSet();
      bucketName = name.getStringValue();
    }

    final int fileId = database.getSchema().getBucketByName(bucketName).getFileId();

    final DocumentType type = database.getSchema().getTypeByBucketName(bucketName);
    if (type != null)
      throw new CommandExecutionException("Cannot drop bucket '" + bucketName + "' because used by type '" + type.getName() + "'");

    // REMOVE CACHE OF COMMAND RESULTS IF ACTIVE
    database.getSchema().dropBucket(bucketName);

    final InternalResultSet rs = new InternalResultSet();
    final ResultInternal result = new ResultInternal();
    result.setProperty("operation", "drop bucket");
    result.setProperty("bucketName", bucketName);
    result.setProperty("bucketId", fileId);
    rs.add(result);
    return rs;
  }

  @Override
  public void toString(final Map<String, Object> params, final StringBuilder builder) {
    builder.append("DROP BUCKET ");
    if (name != null)
      name.toString(params, builder);
    else
      id.toString(params, builder);

    if (ifExists)
      builder.append(" IF EXISTS");
  }

  @Override
  public DropBucketStatement copy() {
    final DropBucketStatement result = new DropBucketStatement(-1);
    result.name = name == null ? null : name.copy();
    result.id = id == null ? null : id.copy();
    result.ifExists = this.ifExists;
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    final DropBucketStatement that = (DropBucketStatement) o;

    if (ifExists != that.ifExists)
      return false;
    else if (!Objects.equals(name, that.name))
      return false;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (id != null ? id.hashCode() : 0);
    result = 31 * result + (ifExists ? 1 : 0);
    return result;
  }
}
/* JavaCC - OriginalChecksum=239ffe92e79e1d5c82976ed9814583ec (do not edit this line) */
