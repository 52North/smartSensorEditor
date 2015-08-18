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

import de.conterra.smarteditor.clients.ClientFactory;
import de.conterra.smarteditor.clients.GenericClient;
import de.conterra.smarteditor.clients.PostClient;
import de.conterra.smarteditor.clients.Protocol;
import de.conterra.smarteditor.clients.SoapClient;
import de.conterra.smarteditor.dao.CatalogServiceException;
import de.conterra.smarteditor.dao.GenericCatalogService;
import de.conterra.smarteditor.service.XSLTTransformerService;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.w3c.dom.Document;

/**
 * Adapter to publish and discover metadata with geonetwork
 */
public class SOSCatalogService extends GenericCatalogService {

	public String clientId;
	public XSLTTransformerService transformerService;
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	

	public XSLTTransformerService getTransformerService() {
		return transformerService;
	}

	public void setTransformerService(XSLTTransformerService transformerService) {
		this.transformerService = transformerService;
	}

	@Override
	public Document discovery(Document request) throws CatalogServiceException {
		getClient().setEndpoint(super.getEndpoints().get("discovery"));
		setCookie();
		return doRequest(request);
	}

	@Override
	public Document transaction(Document request) throws CatalogServiceException {
		//setCookie();
		GenericClient genericClient =  ClientFactory.createClient(Protocol.valueOf(getClientId()), "http://www.sdisuite.de/soapServices/services/CSWDiscovery");
		if(genericClient.getClass().isInstance(new SoapClient(""))){
			SoapClient soapClient=(SoapClient)genericClient;
			soapClient.setTranformerService(transformerService);
			genericClient=soapClient;
		}
		setClient((PostClient) genericClient);
		getClient().setEndpoint(super.getEndpoints().get("transaction"));
		return doRequest(request);
	}

	private void setCookie() {
		getClient().setEndpoint(super.getEndpoints().get("discovery"));
		/*getClient().setRequestHeader("Cookie", "JSESSIONID=" + sessionId);*/
	}
}
