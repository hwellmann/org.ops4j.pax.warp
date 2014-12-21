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
package org.ops4j.pax.warp.command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.JAXBException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.ops4j.pax.warp.command.impl.CommandRunnerImpl;


/**
 * @author Harald Wellmann
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractCommandRunnerTest {

    protected CommandRunner commandRunner = new CommandRunnerImpl();
    
    protected void dropAndCreateDatabase(String jdbcAdminUrl) throws SQLException {
        Connection dbc = DriverManager.getConnection(jdbcAdminUrl, "warp", "warp");
        Statement st = dbc.createStatement();
        st.executeUpdate("drop database if exists warp");
        st.executeUpdate("create database warp");
        st.close();
        dbc.close();
    }

    protected void updateStructure(String jdbcUrl, String dbms) throws JAXBException, SQLException, IOException {
        InputStream is = getClass().getResourceAsStream("/changelogs/changelog1.xml");
        Connection dbc = DriverManager.getConnection(jdbcUrl, "warp", "warp");
        commandRunner.update(dbc, is, dbms);
        dbc.close();
    }

    protected void updateData(String jdbcUrl, String dbms) throws JAXBException, SQLException, IOException {
        InputStream is = getClass().getResourceAsStream("/changelogs/data1.xml");
        Connection dbc = DriverManager.getConnection(jdbcUrl, "warp", "warp");
        commandRunner.update(dbc, is, dbms);
        dbc.close();
    }

    protected void dumpData(String jdbcUrl, String dbms) throws JAXBException, SQLException, IOException {
        Connection dbc = DriverManager.getConnection(jdbcUrl, "warp", "warp");
        OutputStream os = new FileOutputStream("target/data1.xml");
        commandRunner.dumpData(dbc, os);
        os.close();
        dbc.close();
    }
    
    protected abstract String getJdbcUrl();

    protected abstract String getJdbcAdminUrl();

    protected abstract String getDbms();
    
    @Test
    public void test01ShouldUpdate() throws JAXBException, SQLException, IOException {
        String adminUrl = getJdbcAdminUrl();
        if (adminUrl != null) {
            dropAndCreateDatabase(adminUrl);
        }
        updateStructure(getJdbcUrl(), getDbms());
    }

    @Test
    public void test02ShouldUpdateData() throws JAXBException, SQLException, IOException {
        updateData(getJdbcUrl(), getDbms());
    }

    @Test
    public void test03ShouldDumpData() throws JAXBException, SQLException, IOException {
        dumpData(getJdbcUrl(), getDbms());
    }
    
}
