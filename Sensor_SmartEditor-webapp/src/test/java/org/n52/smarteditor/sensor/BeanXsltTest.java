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

import static org.xmlmatchers.XmlMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xmlmatchers.namespace.SimpleNamespaceContext;

import de.conterra.smarteditor.beans.BaseBean;
/*import de.conterra.smarteditor.edit.services.BeanTransformerTest;*/
import de.conterra.smarteditor.service.BeanTransformerService;
import de.conterra.smarteditor.util.DOMUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/sml-transformer-config.xml",
		"/sml-transformer-config_AcousticSensor.xml" })
public class BeanXsltTest {
	SimpleNamespaceContext usingNamespaces;
	static private Logger LOG = Logger.getRootLogger();

	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	@Resource(name = "smlLongName")
	BaseBean smlLongName;
	@Resource(name = "smlShortName")
	BaseBean smlShortName;
	@Resource(name = "smlUniqueID")
	BaseBean smlUniqueID;
	@Resource(name = "smlIdentifier")
	BaseBean smlIdentifier;

	@Resource(name = "smlAcousticSensorLength")
	BaseBean smlAcousticSensorLength;
	@Resource(name = "smlAcousticSensorWeight")
	BaseBean smlAcousticSensorWeight;
	@Resource(name = "smlAcousticSensorHeight")
	BaseBean smlAcousticSensorHeight;

	@Resource(name = "multiSmlKeyword")
	BaseBean multiSmlKeyword;

	@Resource(name = "multiSmlIdentification")
	BaseBean multiSmlIdentification;

	Document mDatasetDocument = DOMUtil.createFromStream(BeanXsltTest.class
			.getResourceAsStream("/validation/input/testSmlToBeanXSLT.xml"),
			true);
	Document mDatasetDocument_AcousticSensor = DOMUtil
			.createFromStream(
					BeanXsltTest.class
							.getResourceAsStream("/validation/input/testSmlToBeanXSLT_AcousticSensor.xml"),
					true);

	@Before
	public void before() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("sml", "http://www.opengis.net/sensorML/1.0.1");
		map.put("swe", "http://www.opengis.net/swe/1.0.1");
		map.put("gml", "http://schemas.opengis.net/gml/3.1.1/base/gml.xsd");
		usingNamespaces = new SimpleNamespaceContext();
		usingNamespaces.setBindings(map);
	}

/*	*//**
	 * This method tests, if the test-value for longName within the xml document
	 * is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testLongName() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(smlLongName,
				mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/SmlLongName/longName[text()='testname']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for shortName within the xml
	 * document is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testShortName() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(smlShortName,
				mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/SmlShortName/shortName[text()='shortname']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for uniqueID within the xml document
	 * is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testUniqueID() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(smlUniqueID,
				mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/FileIdentifier/identifier[text()='testunique']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for uniqueID within the xml document
	 * is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testKeyword() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(multiSmlKeyword,
				mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("//SmlKeyword/keyword[text()='testkeyword']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for smlAcousticSensor_Length within
	 * the xml document is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testSmlAcousticSensor_Length() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(
				smlAcousticSensorLength, mDatasetDocument_AcousticSensor);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/SmlAcousticSensorLength/length[text()='32']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for smlAcousticSensor_Weight within
	 * the xml document is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testSmlAcousticSensorWeight() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(
				smlAcousticSensorWeight, mDatasetDocument_AcousticSensor);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/SmlAcousticSensorWeight/weight[text()='128']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for smlAcousticSensor_Height within
	 * the xml document is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testSmlAcousticSensorHeight() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(
				smlAcousticSensorHeight, mDatasetDocument_AcousticSensor);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/SmlAcousticSensorHeight/height[text()='6.5']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	*//**
	 * This method tests, if the test-value for smlIdentification within the xml
	 * document is copied into the bean.
	 * 
	 * @throws Exception
	 *//*
	@Test
	public void testSmlIdentification() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(
				multiSmlIdentification, mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("//SmlIdentification/value[text()='testunique']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath("//SmlIdentification/name[text()='shortName']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath(
							"//SmlIdentification/definition[text()='urn:ogc:def:identifier:OGC:1.0:longName']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}*/

	/**
	 * This method tests, if the test-value for smlIdentifier within the xml
	 * document is copied into the bean.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIdentifier() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(smlIdentifier,
				mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/FileIdentifier/identifier[text()='testIdentifier']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}
}
