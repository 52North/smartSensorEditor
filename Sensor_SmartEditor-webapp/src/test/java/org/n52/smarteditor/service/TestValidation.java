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
package org.n52.smarteditor.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.annotation.Resource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/sml-BackendManagerService-config.xml")
public class TestBackendManagerService2 {

	@Resource(name = "backendSetupService")
	BackendManagerService mService;

	@Before
	public void before() {
		mService.setMergeDocument(DOMUtil.createDocumentFromSystemID(
				"classpath:/test-sensor.xml", true));
	}

	@Test
	public void testMergeBackend() {

		String mergedDocument = DOMUtil.convertToString(
				mService.mergeBackend(),false);
		Document lReport = mService.validate("smlValidator");
		String report = DOMUtil.convertToString(lReport, true);
		System.out.println("Report"+report);
	}
/*	@Test
	public void testTransformation() {

		 String formattedOutput = "";
		    try {

		        TransformerFactory tFactory = TransformerFactory.newInstance();            
		        Transformer transformer =
		                tFactory.newTransformer( new StreamSource( "//target//test-classes//validation//iso_svrl_for_xslt2TEST.xsl") );

		        StreamSource xmlSource = new StreamSource(new StringReader("//target//test-classes//test-sensor.xml") );
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        transformer.transform( xmlSource, new StreamResult( baos ) );

		        formattedOutput = baos.toString();

		    } catch( Exception e ) {
		        e.printStackTrace();
		    }

	}*/
	@Test
	public void testTransformation2(){
	
	}
}
