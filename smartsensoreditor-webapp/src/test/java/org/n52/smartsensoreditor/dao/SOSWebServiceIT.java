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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import de.conterra.smarteditor.clients.ClientFactory;
import de.conterra.smarteditor.clients.Protocol;
import de.conterra.smarteditor.clients.RequestFactory;
import de.conterra.smarteditor.clients.SoapClient;
import de.conterra.smarteditor.dao.WebServiceDescriptionException;
import de.conterra.smarteditor.service.XSLTTransformerService;
import de.conterra.smarteditor.util.DOMUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/TestTransformer.xml")
public class SOSWebServiceIT {

	@Resource(name = "SOSCatalogServiceDAO")
	SOSCatalogService sosWebServiceDAO;

	@Resource(name = "xsltTransformerService")
	private XSLTTransformerService xsltTransformerService;

	@Resource(name="requestFactory")
	private RequestFactory requestFactory;

	@Resource(name = "xsltTransformerService")
	private XSLTTransformerService xsltTransformer;

	@Resource(name="transformerFiles" )
	private Map<String, String> transformerFiles;

	private String sensorId = "http://www.52north.org/test/procedure/9";
	private String serviceURL="http://localhost:8081/52n-sos-webapp/service";
	private String endpoint = serviceURL+"/soap";
	private String authorizationToken="test123";
	Map parameterMap =null;
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

		sosWebServiceDAO.init(endpoint);

		sosWebServiceDAO.addRequestHeader("Authorization", authorizationToken);
		parameterMap = new HashMap();
		parameterMap.put("procedureId", sensorId);
	}
	/**
	 * Extracts SensorML from the Soap message
	 */
	private Document transform(Document response){
		//For Transformation
		Document sensorML = DOMUtil.newDocument(true);
		Source source = new DOMSource(response);
		Result result = new DOMResult(sensorML);

		String transformerFilePath=transformerFiles.get("sos");
		xsltTransformer.setRulesetSystemID(transformerFilePath);
		// transform
		xsltTransformer.transform(source, result);
		return sensorML;
	}
	public String getSensor() throws Exception{
		sosWebServiceDAO.init(endpoint);

		Document catalogRequest = requestFactory.createRequest("get" , parameterMap);
		Document catalogResponse = sosWebServiceDAO.transaction(catalogRequest);
		
		if(catalogResponse==null) {
			throw new Exception();
		}
		
		//Extract the sensorML from the Soap message
		Document sensorML=transform(catalogResponse);
		String docString = DOMUtil.convertToString(sensorML, true);
	
		return docString;
	}
	//Concept from de.conterra.smarteditor.clients.SoapClientTest
	public String insertSensor() throws Exception {
		sosWebServiceDAO.init(endpoint);

		//get the test sensor data
		URL url = getClass().getResource("/requests/insertTestSensorSoap.xml");
		File xmlFile = new File(url.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document request;
		request = dBuilder.parse(xmlFile);
		
		//create Request
		request=requestFactory.createRequest("insert", request);
		
		// insert sensor 
		Document catalogResponse =sosWebServiceDAO.transaction(request);
		
		if(catalogResponse==null) {
			throw new Exception();
		}
		String docString = DOMUtil.convertToString(catalogResponse, true);

		return docString;

	}
	//Concept from de.conterra.smarteditor.clients.SoapClientTest
	public String updateSensor() throws Exception {
		sosWebServiceDAO.init(endpoint);

		//get the test sensor data
		URL url = getClass().getResource("/requests/updateTestSensorSoap.xml");
		File xmlFile = new File(url.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document testSensor;
		testSensor = dBuilder.parse(xmlFile);
		
		//Create Request
		Document request=requestFactory.createRequest("update", testSensor);
		
		// update sensor
		Document catalogResponse =sosWebServiceDAO.transaction(request);

		if(catalogResponse==null) {
			throw new Exception();
		}
		//Extract the SensorML from the Soap message
		Document sensorML=transform(catalogResponse);
		String docString = DOMUtil.convertToString(sensorML, true);

		return docString;

	}

	public String deleteSensor() throws Exception{
		String docString="";
		sosWebServiceDAO.init(endpoint);
        //create Request and send to SOS
		Document catalogRequest = requestFactory.createRequest("delete" , parameterMap);
		Document catalogResponse = sosWebServiceDAO.transaction(catalogRequest);
		
		if(catalogResponse==null) {
			throw new Exception();
		}
		docString = DOMUtil.convertToString(catalogResponse, true);
		return docString;
		
	}
	@Test
	public void testGetDescription() throws Exception {
		String docString="";
		try {
			docString=getSensor();
		} catch (Exception e) {
			insertSensor();
			docString=getSensor();
		}

		assertThat(docString, not(containsString("DescribeSensorResponse")));
		assertThat(docString, containsString("sml:PhysicalSystem"));
		assertThat(docString, containsString(sensorId));

	}
	@Test
	public void testInsertSensor() throws Exception {
		String docString="";
		try {
			deleteSensor();
		} catch (Exception e) {	
		}
		//insert sensor and test if the data were inserted
		insertSensor();
		docString= getSensor();
		assertThat(docString, containsString("beforeUpdate"));
	}

	@Test
	public void testUpdateSensor() throws Exception {
		String docString="";
		try {
			deleteSensor();
		} catch (Exception e) {	
		}
		//insert sensor and test the value which will be checked
		insertSensor();
		docString= getSensor();
		assertThat(docString, containsString("beforeUpdate"));

		//update the sensor data and check if the values were updated
		updateSensor();
		docString= getSensor();
		assertThat(docString, not(containsString("beforeUpdate")));
		assertThat(docString, containsString("Test_longName_Update"));
	}
	@Test
	public void testDeleteSensor() throws Exception {
		String docString="";
		try {
			docString=deleteSensor();
		} catch (Exception e) {
			insertSensor();
			docString=deleteSensor();
		}
	
		assertThat(docString, containsString("swes:deletedProcedure"));
		assertThat(docString, containsString("http://www.52north.org/test/procedure/9"));
	}

	@After
	public void finish(){
		try {
			deleteSensor();
		} catch (Exception e) {
		}
	}
}
