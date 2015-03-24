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
package org.n52.smartsensoreditor.dao;

import de.conterra.smarteditor.clients.GetClient;
import de.conterra.smarteditor.clients.PostClient;
import de.conterra.smarteditor.dao.WebServiceDescriptionDAO;
import de.conterra.smarteditor.dao.WebServiceDescriptionException;
import de.conterra.smarteditor.util.DOMUtil;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets the description of OGC Web services
 *
 * @author kse
 *         Date: 19.07.11
 *         Time: 12:28
 */
public class SOSWebServiceDescriptionDAO extends WebServiceDescriptionDAO {

    private static Logger LOG = Logger.getLogger(SOSWebServiceDescriptionDAO.class);
    private Map<String, String> transformerFiles;
    private String serviceTokenForSOS;
    private String serviceProcedureIDForSOS;
    
    public String getServiceTokenForSOS() {
		return this.serviceTokenForSOS;
	}

	public void setServiceTokenForSOS(String serviceTokenForSOS) {
		this.serviceTokenForSOS = serviceTokenForSOS;
	}
	public String getServiceProcedureIDForSOS() {
		return serviceProcedureIDForSOS;
	}

	public void setServiceProcedureIDForSOS(String serviceProcedureIDForSOS) {
		this.serviceProcedureIDForSOS = serviceProcedureIDForSOS;
	}

    @Override
    public Document getDescription() throws WebServiceDescriptionException {
        GetClient client = new GetClient(getUrl());
        Map<String, String> queryString = new HashMap<String, String>();
        queryString.put("service", getServiceType());
        queryString.put("request", "DescribeSensor");
        queryString.put("procedure", getServiceProcedureIDForSOS());
        queryString.put("version","2.0.0");
        queryString.put("procedureDescriptionFormat","http://www.opengis.net/sensorml/2.0");
        client.addRequestHeader("Content-Type", "application/x-kvp");
        try {
            String content = client.invoke(queryString);
            getXsltTransformer().setRulesetSystemID(getTemplateName());
            Document doc = DOMUtil.newDocument(true);
           Source source = new DOMSource(DOMUtil.createFromString(content, true));
            Result result = new DOMResult(doc);
            // transform
            getXsltTransformer().transform(source, result);
            return doc;
        } catch (TransformerException e) {
            LOG.error(e.getMessage(), e);
            throw new WebServiceDescriptionException(e);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new WebServiceDescriptionException(e);
        }
    }
    public Map<String, String> getTransformerFiles() {
        return transformerFiles;
    }

    public void setTransformerFiles(Map<String, String> transformerFiles) {
        this.transformerFiles = transformerFiles;
    }

    @Override
    public String getTemplateName() {
        if (getServiceType() == null) {
            return null;
        }
        return getTransformerFiles().get(getServiceType().toLowerCase());
    }
}
