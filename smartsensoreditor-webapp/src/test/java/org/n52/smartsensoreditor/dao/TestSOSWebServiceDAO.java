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

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/TestTransformer.xml")
public class TestSOSWebServiceDAO {
	@Resource(name = "sosWebServiceDAO")
	SOSWebServiceDescriptionDAO sosWebServiceDAO;
	
	
	@Before
	public void before(){
		sosWebServiceDAO.setUrl("http://localhost:8080/52n-sos-webapp/service");
		sosWebServiceDAO.setServiceProcedureIDForSOS("http://www.52north.org/test/procedure/9");
		sosWebServiceDAO.setServiceType("SOS");
	}
	@Test
	public void testTransformer() {
	Document doc=sosWebServiceDAO.getDescription();
	String docString = DOMUtil.convertToString(
			doc, true);
	System.out.println(docString);
	assertThat(docString, not(containsString("DescribeSensorResponse")));
	}
}
