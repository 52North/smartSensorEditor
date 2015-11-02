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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.SAXException;

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
	private String serviceURL="";
	private String authorizationToken="";
	Map parameterMap =null;
	@Before
	public void before()  {
		//Get from propertyFile
		Properties properties = new Properties();
		String userHome=System.getProperty("user.home");
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(userHome+"/build.properties"));
			properties.load(stream);
			stream.close();
			if(properties.getProperty("IT_serviceURL")!=null){
				serviceURL=properties.getProperty("IT_serviceURL");
			}
			if(properties.getProperty("IT_authorizationToken")!=null){
				authorizationToken=properties.getProperty("IT_authorizationToken");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		parameterMap = new HashMap();
		parameterMap.put("procedureId", sensorId);

		deleteSensor();
		insertSensor();
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
	public String getSensor(){
		sosWebServiceDAO.init(serviceURL);
		sosWebServiceDAO.addRequestHeader("Authorization", authorizationToken);
		sosWebServiceDAO.addRequestHeader("Content-Type", "application/soap+xml");

		Document catalogRequest = requestFactory.createRequest("get" , parameterMap);
		Document catalogResponse = sosWebServiceDAO.transaction(catalogRequest);

		if(catalogResponse==null) {
			return null;
		}

		//Extract the sensorML from the Soap message
		Document sensorML=transform(catalogResponse);
		String docString = DOMUtil.convertToString(sensorML, true);

		return docString;
	}
	//Concept from de.conterra.smarteditor.clients.SoapClientTest
	public String insertSensor() {
		sosWebServiceDAO.init(serviceURL);
		sosWebServiceDAO.addRequestHeader("Authorization", authorizationToken);
		sosWebServiceDAO.addRequestHeader("Content-Type", "application/soap+xml");
		//get the test sensor data
		URL url = getClass().getResource("/requests/insertTestSensorSoap.xml");
		File xmlFile = new File(url.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document request;
			request = dBuilder.parse(xmlFile);
			//create Request
			Map<String, Object> parameterMap=new  HashMap<String, Object>();
	        String [] swesObservablePropertyList=toList("http://www.52north.org/test/observableProperty/9_1");
			parameterMap.put("swesObservablePropertyList",swesObservablePropertyList);
			
			 String [] sosObservationTypeList=toList("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
			 parameterMap.put("sosObservationTypeList",sosObservationTypeList);
			 
			 String [] sosFeatureOfInterestTypeList=toList("http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint");
			 parameterMap.put("sosFeatureOfInterestTypeList",sosFeatureOfInterestTypeList);
			
			request=requestFactory.createRequest("insert", request,parameterMap);

			// insert sensor 
			Document catalogResponse =sosWebServiceDAO.transaction(request);

			if(catalogResponse==null) {
				return null;
			}
			String docString = DOMUtil.convertToString(catalogResponse, true);

			return docString;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	//Concept from de.conterra.smarteditor.clients.SoapClientTest
	public String updateSensor()  {
		sosWebServiceDAO.init(serviceURL);
		sosWebServiceDAO.addRequestHeader("Authorization", authorizationToken);
		sosWebServiceDAO.addRequestHeader("Content-Type", "application/soap+xml");
		//get the test sensor data
		URL url = getClass().getResource("/requests/updateTestSensorSoap.xml");
		File xmlFile = new File(url.getPath());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document testSensor;
			testSensor = dBuilder.parse(xmlFile);

			//Create Request
			Document request=requestFactory.createRequest("update", testSensor);

			// update sensor
			Document catalogResponse =sosWebServiceDAO.transaction(request);

			if(catalogResponse==null) {
				return null;
			}
			//Extract the SensorML from the Soap message
			Document sensorML=transform(catalogResponse);
			String docString = DOMUtil.convertToString(sensorML, true);

			return docString;

		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public String deleteSensor(){
		String docString="";
		sosWebServiceDAO.init(serviceURL);
		sosWebServiceDAO.addRequestHeader("Authorization", authorizationToken);
		sosWebServiceDAO.addRequestHeader("Content-Type", "application/soap+xml");
		
		//create Request and send to SOS
		Document catalogRequest = requestFactory.createRequest("delete" , parameterMap);
		Document catalogResponse = sosWebServiceDAO.transaction(catalogRequest);

		if(catalogResponse==null) {
			return null;
		}
		docString = DOMUtil.convertToString(catalogResponse, true);
		return docString;

	}
	@Test
	public void testGetDescription() throws Exception {
		String docString="";

		docString=getSensor();
		if(docString==null){
			throw new RuntimeException("docString returns null. Perhaps HTTP 400 response, which is catched in invoke() in PostClient.java. See SOAP message in Log-File.");
		}
		assertThat(docString, not(containsString("DescribeSensorResponse")));
		assertThat(docString, containsString("sml:PhysicalSystem"));
		assertThat(docString, containsString(sensorId));

	}
	@Test
	public void testInsertSensor() throws RuntimeException {
		String docString="";

		docString= getSensor();
		if(docString==null){
			throw new RuntimeException("docString returns null. Perhaps HTTP 400 response, which is catched in invoke() in PostClient.java. See SOAP message in Log-File.");
		}
		assertThat(docString, containsString("beforeUpdate"));
	}

	@Test
	public void testUpdateSensor() throws RuntimeException {
		String docString="";

		docString= getSensor();
		if(docString==null){
			throw new RuntimeException("docString returns null. Perhaps HTTP 400 response, which is catched in invoke() in PostClient.java. See SOAP message in Log-File.");
		}
		assertThat(docString, containsString("beforeUpdate"));

		//update the sensor data and check if the values were updated
		updateSensor();
		docString= getSensor();
		if(docString==null){
			new RuntimeException("docString returns null. Perhaps HTTP 400 response, which is catched in invoke() in PostClient.java. See SOAP message in Log-File.");
		}
		assertThat(docString, not(containsString("beforeUpdate")));
		assertThat(docString, containsString("Test_longName_Update"));
	}
	@Test
	public void testDeleteSensor() throws RuntimeException {
		String docString="";

		docString=deleteSensor();
		if(docString==null){
			throw new RuntimeException("docString returns null. Perhaps HTTP 400 response, which is catched in invoke() in PostClient.java. See SOAP message in Log-File.");
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
	private String[] toList(String values){
		  values= values.replaceAll("\\s", "");
	      String[] list=values.split(",");
	      return list;
	}
}
