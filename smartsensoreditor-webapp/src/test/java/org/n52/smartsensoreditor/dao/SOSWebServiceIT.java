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

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import de.conterra.smarteditor.clients.ClientFactory;
import de.conterra.smarteditor.clients.Protocol;
import de.conterra.smarteditor.clients.SoapClient;
import de.conterra.smarteditor.dao.WebServiceDescriptionException;
import de.conterra.smarteditor.service.XSLTTransformerService;
import de.conterra.smarteditor.util.DOMUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/TestTransformer.xml")
public class SOSWebServiceIT {

	@Resource(name = "sosWebServiceDAO")
	SOSWebServiceDAO sosWebServiceDAO;

	private String sensorId = "http://www.52north.org/test/procedure/9";
	private String serviceURL="http://localhost:8080/52n-sos-webapp_4.3.0/service";
	private String endpoint = serviceURL+"/soap";
	private String authorizationToken="test123";

	@Resource(name = "xsltTransformerService")
	private XSLTTransformerService xsltTransformerService;

	@Before
	public void before() throws Exception {
		//Get from propertyFile
		Properties properties = new Properties();
		String userHome=System.getProperty("user.home");
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(userHome+"/build.properties"));
		properties.load(stream);
		stream.close();
		if(properties.getProperty("IT_serviceURL")!=null){
			serviceURL=properties.getProperty("IT_serviceURL");
		}
		if(properties.getProperty("IT_authorizationToken")!=null){
			authorizationToken=properties.getProperty("IT_authorizationToken");
		}
		
		sosWebServiceDAO.setUrl(serviceURL);
		sosWebServiceDAO.setServiceProcedureIDForSOS(sensorId);
		
		sosWebServiceDAO.setServiceType("SOS");
	}

	public void insertSensor() throws Exception {
		URL url = getClass().getResource("/requests/insertTestSensorSoap.xml");
		File xmlFile = new File(url.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document request;
		request = dBuilder.parse(xmlFile);
		// insert sensor post
		SoapClient client = (SoapClient) ClientFactory.createClient(
				Protocol.HTTP_SOAP, endpoint);
		client.addRequestHeader("Authorization", authorizationToken);
		client.setTranformerService(xsltTransformerService);
		client.setPayload(DOMUtil.convertToString(request, true));

		client.invoke(null);
		// System.out.println(s);
	}

	@Test
	public void testTransformer() throws Exception {
		String docString="";
		try {
		Document doc = sosWebServiceDAO.getDescription();
	    docString = DOMUtil.convertToString(doc, true);
		
		} catch (Exception e) {
			if(e.toString().contains("is invalid")){
				insertSensor();
				Document doc = sosWebServiceDAO.getDescription();
				docString = DOMUtil.convertToString(doc, true);
			}
		}
		assertThat(docString, not(containsString("DescribeSensorResponse")));
		assertThat(docString, containsString("sml:PhysicalSystem"));
		assertThat(docString, containsString(sensorId));
		
	}

	
	public void removeSensor() throws Exception {
		URL url = getClass().getResource("/requests/deleteTestSensorSoap.xml");
		File xmlFile = new File(url.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document request;
		request = dBuilder.parse(xmlFile);// insert sensor post
		SoapClient client = (SoapClient) ClientFactory.createClient(
				Protocol.HTTP_SOAP, endpoint);
		client.addRequestHeader("Authorization", authorizationToken);
		client.setTranformerService(xsltTransformerService);
		//System.out.println("String:"+DOMUtil.convertToString(request, true));
		client.setPayload(DOMUtil.convertToString(request, true));
		client.invoke(null);
	    
	}
	@After
    public void finish() throws Exception{
    	removeSensor();
    }
}
