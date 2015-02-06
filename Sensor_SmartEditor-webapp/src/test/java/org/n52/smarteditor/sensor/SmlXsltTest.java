/**
 * Copyright (C) ${inceptionYear}-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.smarteditor.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.hasXPath;

import java.util.HashMap;

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
import de.conterra.smarteditor.service.BeanTransformerService;
import de.conterra.smarteditor.util.DOMUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/sml-transformer-config.xml" , "/sml-transformer-config_AcousticSensor.xml"})
public class SmlXsltTest {
	static private Logger LOG = Logger.getRootLogger();
	SimpleNamespaceContext usingNamespaces;
	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	Document mRefDatasetDocument = DOMUtil.createFromStream(
			SmlXsltTest.class.getResourceAsStream("/validation/input/testBeanTOSmlXSLT.xml"), true);
	
	Document mRefDatasetDocument_AcousticSensor = DOMUtil.createFromStream(
			BeanXsltTest.class.getResourceAsStream("/validation/input/testBeanTOSmlXSLT_AcousticSensor.xml"), true);
	@Resource(name = "smlLongName")
	BaseBean smlLongName;
	
	@Resource(name = "smlShortName")
	BaseBean smlShortName;
     
	@Resource(name = "smlUniqueID")
	BaseBean smlUniqueID;
	
	@Resource(name = "smlKeyword")
	BaseBean smlKeyword;
	
	@Resource(name = "smlAcousticSensorLength")
	BaseBean smlAcousticSensorLength;
	@Resource(name = "smlAcousticSensorWeight")
	BaseBean smlAcousticSensorWeight;
	@Resource(name = "smlAcousticSensorHeight")
	BaseBean smlAcousticSensorHeight;
	

	@Resource(name = "multiSmlKeyword")
    MultipleElementBean multiSmlKeyword;
	@Before
	public void before() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("sml", "http://www.opengis.net/sensorML/1.0.1");
		map.put("swe", "http://www.opengis.net/swe/1.0.1");
		usingNamespaces = new SimpleNamespaceContext();
		usingNamespaces.setBindings(map);
	}

	/**
	 * This method tests, if the test-value for longName is copied into the xml document.
	 */
	@Test
	public void testLongName() {
		//
		BeanUtil.setProperty(smlLongName, "longName", "testname");
		//
		Document doc = beanTransformerService.mergeToISO(smlLongName,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:longName']/sml:value[text()='testname']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	@Test
	public void testShortName() {
		BeanUtil.setProperty(smlShortName, "shortName", "shortname");
		Document doc = beanTransformerService.mergeToISO(smlShortName,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:shortName']/sml:value[text()='shortname']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	@Test
	public void testUniqueID() {
		BeanUtil.setProperty(smlUniqueID, "id", "testunique");
		
		Document doc = beanTransformerService.mergeToISO(smlUniqueID,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:uniqueID']/sml:value[text()='testunique']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	
	@Test
	public void testKeyword() {
	
		BeanUtil.setProperty(smlKeyword, "keyword","testkeyword");
		multiSmlKeyword.getItems().add(smlKeyword);
		Document doc = beanTransformerService.mergeToISO(multiSmlKeyword,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:keywords/sml:KeywordList/sml:keyword[text()='testkeyword']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	 * This method tests, if the test-value for SmlAcousticSensorLength is copied into the xml document.
	 */
	@Test
	public void testSmlAcousticSensorLength() {
		//
		BeanUtil.setProperty(smlAcousticSensorLength, "length", "10");
		//
		Document doc = beanTransformerService.mergeToISO(smlAcousticSensorLength,
				mRefDatasetDocument_AcousticSensor);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:characteristics/swe:DataRecord[@definition='urn:ogc:def:property:OGC:physicalProperties']/swe:field/swe:DataRecord/swe:field/swe:Quantity[@definition='urn:ogc:def:property:OGC:length']/swe:value[text()='10']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	
	/**
	 * This method tests, if the test-value for SmlAcousticSensorHeight is copied into the xml document.
	 */
	@Test
	public void testSmlAcousticSensorHeight() {
		//
		BeanUtil.setProperty(smlAcousticSensorHeight, "height", "15");
		//
		Document doc = beanTransformerService.mergeToISO(smlAcousticSensorHeight,
				mRefDatasetDocument_AcousticSensor);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:characteristics/swe:DataRecord[@definition='urn:ogc:def:property:OGC:physicalProperties']/swe:field/swe:DataRecord/swe:field/swe:Quantity[@definition='urn:ogc:def:property:OGC:height']/swe:value[text()='15']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	
	/**
	 * This method tests, if the test-value for SmlAcousticSensorWeight is copied into the xml document.
	 */
	@Test
	public void testSmlAcousticSensorWeight() {
		//
		BeanUtil.setProperty(smlAcousticSensorWeight, "weight", "20");
		//
		Document doc = beanTransformerService.mergeToISO(smlAcousticSensorWeight,
				mRefDatasetDocument_AcousticSensor);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:characteristics/swe:DataRecord[@definition='urn:ogc:def:property:OGC:physicalProperties']/swe:field/swe:DataRecord/swe:field/swe:Quantity[@definition='urn:ogc:def:property:OGC:weight']/swe:value[text()='20']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
}
