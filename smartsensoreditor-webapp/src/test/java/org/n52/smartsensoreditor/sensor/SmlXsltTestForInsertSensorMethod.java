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
package org.n52.smartsensoreditor.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.hasXPath;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import jodd.bean.BeanUtil;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xmlmatchers.namespace.SimpleNamespaceContext;

import de.conterra.smarteditor.beans.BaseBean;
import de.conterra.smarteditor.beans.MultipleElementBean;
import de.conterra.smarteditor.clients.RequestFactory;
import de.conterra.smarteditor.service.BeanTransformerService;
import de.conterra.smarteditor.util.DOMUtil;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/sml-InsertSensor-config.xml"})
public class SmlXsltTestForInsertSensorMethod {
	static private Logger LOG = Logger.getRootLogger();
	SimpleNamespaceContext usingNamespaces;

	@Resource(name = "requestFactory")
	private RequestFactory requestFactory;
	Map<String, Object> parameterMap;

	Document doc = DOMUtil.createFromStream(
			SmlXsltTestForInsertSensorMethod.class.getResourceAsStream("/requests/insertTestSensorSoap.xml"), true);

	String testSwesObservableProperty="http://www.52north.org/test/observableProperty/9_1,http://www.52north.org/test/observableProperty/9_2,http://www.52north.org/test/observableProperty/9_3";
	String testSOSObservationType="http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement,http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation,http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
	String testSOSFeatureOfInterestType="http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint,http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint2";

	private String[] toList(String values){
		values= values.replaceAll("\\s", "");
		String[] list=values.split(",");
		return list;
	}

	@Before
	public void before() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("sml", "http://www.opengis.net/sensorml/2.0");
		map.put("swe", "http://www.opengis.net/swe/2.0");
		map.put("gml", "http://www.opengis.net/gml/3.2");
		map.put("swes", "http://www.opengis.net/swes/2.0");    
		map.put("env", "http://www.w3.org/2003/05/soap-envelope");
		map.put("sos", "http://www.opengis.net/sos/2.0");
		usingNamespaces = new SimpleNamespaceContext();
		usingNamespaces.setBindings(map);

		parameterMap=new  HashMap<String, Object>();
		String [] swesObservablePropertyList=toList(testSwesObservableProperty);
		parameterMap.put("swesObservablePropertyList",swesObservablePropertyList);
		String [] sosObservationTypeList=toList(testSOSObservationType);
		parameterMap.put("sosObservationTypeList",sosObservationTypeList);
		String [] sosFeatureOfInterestTypeList=toList(testSOSFeatureOfInterestType);
		parameterMap.put("sosFeatureOfInterestTypeList",sosFeatureOfInterestTypeList);
	}

	/**

	 */
	@Test
	public void testInsertSWESObservableProperty() {
		// create request
		Document catalogRequest = requestFactory.createRequest("insert" , doc, parameterMap);
		Source request = new DOMSource(catalogRequest);

		try {
			assertThat(
					request,
					hasXPath(
							"//swes:observableProperty[text()='http://www.52north.org/test/observableProperty/9_1']",
							usingNamespaces));
			assertThat(
					request,
					hasXPath(
							"//swes:observableProperty[text()='http://www.52north.org/test/observableProperty/9_2']",
							usingNamespaces));
		}
		catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
		System.out.println("testInsertSWESObservableProperty:"+"\n"+ DOMUtil.convertToString(catalogRequest, true));
	}


	@Test
	public void testInsertSOSObservationType() {
		// create request
		Document catalogRequest = requestFactory.createRequest("insert" , doc, parameterMap);
		Source request = new DOMSource(catalogRequest);
		try {
			assertThat(
					request,
					hasXPath(
							"//sos:observationType[text()='http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement']",
							usingNamespaces));
			assertThat(
					request,
					hasXPath(
							"//sos:observationType[text()='http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation']",
							usingNamespaces));
		}
		catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
		System.out.println("testInsertSOSObservationType:"+"\n"+ DOMUtil.convertToString(catalogRequest, true));
	}
	@Test
	public void testInsertSOSFeatureOfInterestType() {
		// create request
		Document catalogRequest = requestFactory.createRequest("insert" , doc, parameterMap);
		Source request = new DOMSource(catalogRequest);
		try {
			assertThat(
					request,
					hasXPath(
							"//sos:featureOfInterestType[text()='http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint']",
							usingNamespaces));
			assertThat(
					request,
					hasXPath(
							"//sos:featureOfInterestType[text()='http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint2']",
							usingNamespaces));
		}
		catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
		System.out.println("testInsertSOSObservationType:"+"\n"+ DOMUtil.convertToString(catalogRequest, true));
	}
}