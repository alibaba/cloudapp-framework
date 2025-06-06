/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.cloudapp.datasource.druid;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionRememberSetFilter extends FilterAdapter {

    private static final Boolean PRESENT = Boolean.TRUE;

    private Map<DruidPooledConnection, Boolean> activeConnections = new ConcurrentHashMap<>();

    @Override
    public void dataSource_releaseConnection(FilterChain chain, DruidPooledConnection connection) throws SQLException {
        try {
            chain.dataSource_recycle(connection);
        } finally {
            activeConnections.remove(connection);
        }

    }

    public Collection<DruidPooledConnection> getActiveConnections() {
        return Collections.unmodifiableSet(activeConnections.keySet());
    }

    @Override
    public DruidPooledConnection dataSource_getConnection(FilterChain chain, DruidDataSource dataSource,
                                                          long maxWaitMillis) throws SQLException {
        DruidPooledConnection connection = chain.dataSource_connect(dataSource, maxWaitMillis);

        activeConnections.put(connection, PRESENT);
        return connection;
    }
}
