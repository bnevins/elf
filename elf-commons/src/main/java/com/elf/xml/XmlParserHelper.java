/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Byron Nevins
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 */
package com.elf.xml;

import java.io.*;
import javax.xml.stream.*;

/**
 * A place to put all the ugly boiler plate for parsing an XML file.
 * @author Byron Nevins
 */
public final class XmlParserHelper {

    public XmlParserHelper(final File f) throws FileNotFoundException, XMLStreamException {
        stream = new FileInputStream(f);
        parser = XMLInputFactory.newInstance().createXMLStreamReader(
                f.toURI().toString(), stream);
    }

    public final XMLStreamReader get() {
        return parser;
    }

    /**
     * Don't forget to call this method when finished!
     * Closes the parser and the stream
     */
    public final void stop() {
        // yes -- you **do** need to close BOTH of them!
        try {
            if (parser != null) {
                parser.close();
            }
        }
        catch (Exception e) {
            // ignore
        }
        try {
            if (stream != null) {
                stream.close();
            }
        }
        catch (Exception e) {
            // ignore
        }
    }

    private final FileInputStream stream;
    private final XMLStreamReader parser;
}
