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
package org.ops4j.pax.warp.changelog.impl;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.ops4j.pax.warp.jaxb.ChangeLog;
import org.ops4j.pax.warp.util.Exceptions;
import org.xml.sax.SAXException;

public abstract class AbstractChangeLogProcessor {

    protected JAXBContext context;
    protected Schema schema;

    public AbstractChangeLogProcessor() {
        try {
            context = JAXBContext.newInstance(ChangeLog.class);
            loadSchema();
        }
        catch (JAXBException | SAXException | IOException exc) {
            throw Exceptions.unchecked(exc);
        }
    }

    private void loadSchema() throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        URL url = getClass().getResource("/xsd/warp.xsd");
        schema = schemaFactory.newSchema(new Source[] { new StreamSource(url.openStream()) });
    }
}
