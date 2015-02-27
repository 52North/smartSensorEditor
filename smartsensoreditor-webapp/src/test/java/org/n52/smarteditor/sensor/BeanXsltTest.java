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
package org.n52.smarteditor.sensor;

import static org.xmlmatchers.XmlMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.junit.Assert;
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
@ContextConfiguration({ "/sml-transformer-config.xml"})
public class BeanXsltTest {
	SimpleNamespaceContext usingNamespaces;
	static private Logger LOG = Logger.getRootLogger();

	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	
	@Resource(name = "smlIdentifier")
	BaseBean smlIdentifier;


	@Resource(name = "multiSmlKeyword")
	BaseBean multiSmlKeyword;

	@Resource(name = "multiSmlIdentification")
	BaseBean multiSmlIdentification;
	
	@Resource(name = "multiSweQuantityCharacteristic")
	BaseBean multiSweQuantityCharacteristic;
	

	Document mDatasetDocument = DOMUtil.createFromStream(BeanXsltTest.class
			.getResourceAsStream("/validation/input/testSmlToBeanXSLT.xml"),
			true);

	/**
	 * This method tests, if the test-value for keyword within the xml document
	 * is copied into the bean.
	 * 
	 * @throws Exception
	 */
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
					hasXPath("//SmlKeyword/keyword[text()='Ocean Acoustics']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}



	/**
	 * This method tests, if the test-value for smlIdentification within the xml
	 * document is copied into the bean.
	 * 
	 * @throws Exception
	 */
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
					hasXPath(
							"//SmlIdentification/definition[text()='http://www.nexosproject.eu/dictionary/definitions.html#longName']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath("//SmlIdentification/name[text()='Short name']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath("//SmlIdentification/value[text()='C41969B6-F170-0501-432E-B43D5420140B']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

	/**
	 * This method tests, if the test-value for smlIdentification within the xml
	 * document is copied into the bean.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSweQuantityCharacteristic() throws Exception {
		// copy test-value into bean
		BaseBean lBean = beanTransformerService.initBean(
				multiSweQuantityCharacteristic, mDatasetDocument);
		Assert.assertNotNull(lBean);
		// transform to xml for testing
		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath(
							"//SweQuantity/definition[text()='http://www.nexosproject.eu/dictionary/definitions.html#weight']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath("//SweQuantity/label[text()='Length (mm)']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath("//SweQuantity/value[text()='36.0']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath(
							"//SweQuantity/uom[text()='mm']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath(
							"//SweQuantity/description[text()='testDescription']",
							usingNamespaces));
			assertThat(
					bean,
					hasXPath(
							"//SweQuantity/identifier[text()='testIdentifierQuantity']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}
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
