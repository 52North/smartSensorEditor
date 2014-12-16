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
@ContextConfiguration(locations = "classpath:/sml-transformer-config.xml")
public class BeanXsltTest {
	NamespaceContext usingNamespaces;
	static private Logger LOG = Logger.getRootLogger();

	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	@Resource(name = "smlLongName")
	BaseBean smlLongName;
	@Resource(name = "smlShortName")
	BaseBean smlShortName;
	@Resource(name = "smlUniqueID")
	BaseBean smlUniqueID;

	Document mDatasetDocument = DOMUtil.createFromStream(
			BeanXsltTest.class.getResourceAsStream("/validation/input/testBeanXSLT_SmlXSLT.xml"), true);

	@Before
	public void before() {
		usingNamespaces = new SimpleNamespaceContext().withBinding("sml",
				"http://www.opengis.net/sensorML/1.0");
	}

	@Test
	public void testLongName() throws Exception {
		BaseBean lBean = beanTransformerService.initBean(smlLongName,
				mDatasetDocument);
		Assert.assertNotNull(lBean);

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
	@Test
	public void testShortName() throws Exception {
		BaseBean lBean = beanTransformerService.initBean(smlShortName,
				mDatasetDocument);
		Assert.assertNotNull(lBean);

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
	@Test
	public void testUniqueID() throws Exception {
		BaseBean lBean = beanTransformerService.initBean(smlUniqueID,
				mDatasetDocument);
		Assert.assertNotNull(lBean);

		Document beanXML = beanTransformerService.toXML(lBean);
		Source bean = new DOMSource(beanXML);
		try {
			assertThat(
					bean,
					hasXPath("/SmlUniqueID/uniqueID[text()='testunique']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}
	}

}
