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
package org.n52.smartsensoreditor.xml;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import de.conterra.smarteditor.xml.EditorContext;

import java.util.Iterator;

/**
 * Context namespace required by ISO 19139 and schematron
 * @author kse
 * Date: 28.02.2010
 * Time: 13:19:20
 * 
 * TODO: make the EditorContext configurable
 */
public class EditorContextSML extends EditorContext {

    // ISO
    protected static String sNS_GMD = "http://www.isotc211.org/2005/gmd";
    protected static String sNS_GCO = "http://www.isotc211.org/2005/gco";
    protected static String sNS_GML = "http://www.opengis.net/gml/3.2";
    protected static String sNS_GMX = "http://www.isotc211.org/2005/gmx";
    protected static String sNS_GMI = "http://www.isotc211.org/2005/gmi";
    protected static String sNS_GSR = "http://www.isotc211.org/2005/gsr";
    protected static String sNS_GSS = "http://www.isotc211.org/2005/gss";
    protected static String sNS_GTS = "http://www.isotc211.org/2005/gts";
    protected static String sNS_SRV = "http://www.isotc211.org/2005/srv";

    // schematron
    protected static String sNS_SCH = "http://www.ascc.net/xml/schematron";
    protected static String sNS_SVRL = "http://purl.oclc.org/dsdl/svrl";
    protected static String sNS_XLINK = "http://www.w3.org/1999/xlink";
    protected static String sNS_XS = "http://www.w3.org/2001/XMLSchema";

    //config
    protected static String sNS_TC = "http://www.conterra.de/catalog/config/common";
    
    
    protected static String sNS_SML="http://www.opengis.net/sensorml/2.0";
    /**
     * Maps a prefix to a uri
     *
     * @param prefix
     * @return
     */
    public String getNamespaceURI(String prefix) {
        if (prefix.equals("tc")) {
            return sNS_TC;
        } else if (prefix.equals("sch")) {
            return sNS_SCH;
        } else if (prefix.equals("svrl")) {
            return sNS_SVRL;
        } else if (prefix.equals("xlink")) {
            return sNS_XLINK;
        } else if (prefix.equals("xs")) {
            return sNS_XS;
        } else if (prefix.equals("gmd")) {
            return sNS_GMD;
        } else if (prefix.equals("gco")) {
            return sNS_GCO;
        } else if (prefix.equals("gml")) {
            return sNS_GML;
        } else if (prefix.equals("gmi")) {
            return sNS_GMI;
        } else if (prefix.equals("gmx")) {
            return sNS_GMX;
        } else if (prefix.equals("gsr")) {
            return sNS_GSR;
        } else if (prefix.equals("gss")) {
            return sNS_GSS;
        } else if (prefix.equals("gts")) {
            return sNS_GTS;
        } else if (prefix.equals("srv")) {
            return sNS_SRV;
        }else  if (prefix.equals("sml")) {
            return sNS_SML;
        } else {
            return XMLConstants.DEFAULT_NS_PREFIX;
        }
    }

    /**
     * Resolves to a prefix
     *
     * @param namespace
     * @return
     */
    public String getPrefix(String namespace) {
        if (namespace.equals(sNS_TC)) {
            return "tc";
        } else if (namespace.equals(sNS_SCH)) {
            return "sch";
        } else if (namespace.equals(sNS_SVRL)) {
            return "svrl";
        } else if (namespace.equals(sNS_XLINK)) {
            return "xlink";
        } else if (namespace.equals(sNS_XS)) {
            return "xs";
        } else if (namespace.equals(sNS_GCO)) {
            return "gco";
        } else if (namespace.equals(sNS_GMD)) {
            return "gmd";
        } else if (namespace.equals(sNS_GMI)) {
            return "gmi";
        } else if (namespace.equals(sNS_GML)) {
            return "gml";
        } else if (namespace.equals(sNS_GMX)) {
            return "gmx";
        } else if (namespace.equals(sNS_GSR)) {
            return "gsr";
        } else if (namespace.equals(sNS_GSS)) {
            return "gss";
        } else if (namespace.equals(sNS_GTS)) {
            return "gts";
        } else if (namespace.equals(sNS_SRV)) {
            return "srv";
        } else if (namespace.equals(sNS_SML)) {
            return "sml";
        } else {
            return null;
        }
    }

}
   