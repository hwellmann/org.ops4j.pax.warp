/*
 * Copyright 2016 Harald Wellmann.
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
package org.ops4j.pax.warp.core.schema;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;

import org.junit.Before;
import org.ops4j.pax.warp.core.dbms.PostgresDbmsAdapter;

/**
 * @author Harald Wellmann
 *
 */
public class PostgresSchemaHandlerTest extends AbstractSchemaHandlerTest {

    @Before
    public void before() {
        assumeThat(System.getProperty("postgres"), is("true"));
    }

    public PostgresSchemaHandlerTest() {
        super(new PostgresDbmsAdapter());
    }
}
