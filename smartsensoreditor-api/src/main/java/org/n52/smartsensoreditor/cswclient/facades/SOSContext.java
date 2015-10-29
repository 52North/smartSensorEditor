/**
 * Copyright (C) 2014-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.smartsensoreditor.cswclient.facades;

import de.conterra.smarteditor.cswclient.util.Defaults;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.n52.smartsensoreditor.cswclient.util.DefaultsSOS;

/**
 * Class implements the Namespace context for SOS 2.0
 *

 */
public class SOSContext implements NamespaceContext {

    /**
     * Maps a prefix to a uri
     *
     * @param prefix A namespace prefix to test
     * @return the according uri
     */
    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix.equals("ogc")) {
            return Defaults.NAMESPACE_OGC;
        } else if (prefix.equals("ows")) {
            return Defaults.NAMESPACE_OWS;
        } else if (prefix.equals("gml")) {
            return Defaults.NAMESPACE_GML;
        } else if (prefix.equals("swes")) {
            return DefaultsSOS.NAMESPACE_SWES;
        } else if (prefix.equals("soap")) {
            return DefaultsSOS.NAMESPACE_SOAP;
        } else if (prefix.equals("sos")) {
            return DefaultsSOS.NAMESPACE_SOS_20;
        } else {
            return XMLConstants.DEFAULT_NS_PREFIX;
        }
    }

    /**
     * Resolves to a prefix
     *
     * @param namespace a namespace uri
     * @return the according prefix
     */
    @Override
    public String getPrefix(String namespace) {
        if (namespace.equals(Defaults.NAMESPACE_OGC)) {
            return "ogc";
        } else if (namespace.equals(Defaults.NAMESPACE_OWS)) {
            return "ows";
        } else if (namespace.equals(Defaults.NAMESPACE_GML)) {
            return "gml";
        } else if (namespace.equals(DefaultsSOS.NAMESPACE_SOAP)) {
            return "soap";
        } else if (namespace.equals(DefaultsSOS.NAMESPACE_SOS_20)) {
            return "sos";
        } else {
            return null;
        }
    }

    /**
     * not implemented yet
     *
     * @param namespace identifier
     * @return null
     */
    @Override
    public Iterator getPrefixes(String namespace) {
        return null;
    }

}
