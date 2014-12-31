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
package org.ops4j.pax.warp.jaxb;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.ops4j.pax.warp.exc.WarpException;
import org.ops4j.pax.warp.jaxb.gen.ChangeLog;
import org.ops4j.pax.warp.scope.CdiApplicationScoped;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.xml.sax.SAXException;

@Component(service = WarpJaxbContext.class)
@Singleton
@CdiApplicationScoped
public class WarpJaxbContext {

    private JAXBContext context;
    private Schema schema;
    private boolean initialized;

    @Activate
    @PostConstruct
    public void init() {
        try {
            context = JAXBContext.newInstance(ChangeLog.class);
            loadSchema();
            initialized = true;
        }
        catch (JAXBException | SAXException | IOException exc) {
            throw new WarpException(exc);
        }
    }

    private void initIfNeeded() {
        if (!initialized) {
            init();
        }
    }

    private void loadSchema() throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        URL url = getClass().getResource("/xsd/warp.xsd");
        schema = schemaFactory.newSchema(new Source[] { new StreamSource(url.openStream()) });
    }

    public Unmarshaller createValidatingUnmarshaller() throws JAXBException {
        initIfNeeded();
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }

    public Marshaller createValidatingMarshaller() throws JAXBException {
        initIfNeeded();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setSchema(schema);
        return marshaller;
    }

    public Marshaller createFragmentMarshaller() throws JAXBException {
        initIfNeeded();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        return marshaller;
    }
}
