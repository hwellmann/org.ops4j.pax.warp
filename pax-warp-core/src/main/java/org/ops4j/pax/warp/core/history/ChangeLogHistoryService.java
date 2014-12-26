/*
 * Copyright 2014 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.warp.core.history;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.ops4j.pax.warp.core.util.Exceptions;
import org.ops4j.pax.warp.jaxb.gen.Column;
import org.ops4j.pax.warp.jaxb.gen.Constraints;
import org.ops4j.pax.warp.jaxb.gen.CreateTable;
import org.ops4j.pax.warp.jaxb.gen.SqlType;
import org.osgi.service.component.annotations.Component;


/**
 * @author Harald Wellmann
 *
 */
@Dependent
@Component(service = ChangeLogHistoryService.class)
public class ChangeLogHistoryService {

    public CreateTable createHistoryTableAction() {
        CreateTable action = new CreateTable();
        action.setTableName("warp_history");
        List<Column> columns = action.getColumn();
        Column id = new Column();
        id.setName("id");
        id.setType(SqlType.VARCHAR);
        id.setLength(20);
        Constraints constraints = new Constraints();
        constraints.setNullable(false);
        id.setConstraints(constraints);
        columns.add(id);

        Column checksum = new Column();
        checksum.setName("checksum");
        checksum.setType(SqlType.CHAR);
        checksum.setLength(64);
        checksum.setConstraints(constraints);
        columns.add(checksum);

        Column executed = new Column();
        executed.setName("executed");
        executed.setType(SqlType.TIMESTAMP);
        columns.add(executed);

        return action;
    }

    public boolean hasMetaDataTable(Connection dbc) throws SQLException {
        DatabaseMetaData metaData = dbc.getMetaData();
        String tableName = "warp_history";
        if (metaData.storesUpperCaseIdentifiers()) {
            tableName = tableName.toUpperCase();
        }
        return hasTable(metaData, tableName);
    }

    private boolean hasTable(DatabaseMetaData metaData, String tableName) throws SQLException {
        boolean result = false;
        ResultSet rs = metaData.getTables(null, null, tableName, new String[] { "TABLE" });
        result = rs.next();
        rs.close();
        return result;

    }

    public ChangeLogHistory readChangeLogHistory(Connection dbc) {
        ChangeLogHistory history = new ChangeLogHistory();
        try {
            Statement st = dbc.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, checksum FROM warp_history");
            while (rs.next()) {
                String id = rs.getString("id");
                String checksum = rs.getString("checksum");
                history.put(id, checksum);
            }
            rs.close();
            st.close();
        }
        catch (SQLException exc) {
            throw Exceptions.unchecked(exc);
        }
        return history;
    }
}
