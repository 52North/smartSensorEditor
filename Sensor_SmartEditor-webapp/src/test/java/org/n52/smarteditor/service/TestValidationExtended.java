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
import java.io.IOException;
import java.io.StringReader;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.service.ValidatorService;
import de.conterra.smarteditor.service.XSLTTransformerService;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.validator.SchematronValidator;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.not;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/sml-BackendManagerService-config_For_Extended_Validation.xml")
public class TestValidationExtended {
	@Resource(name = "xsltTransformerService")
	XSLTTransformerService xsltTransformer;

	/*
	 * @Resource(name = "backendSetupService") BackendManagerService mService;
	 */

	private static SchematronValidator validator;

	private static DocumentBuilder documentBuilder;

	private static final String rulesetSystemID = "/validation/ExtendedSensorML_Profile_for_Discovery.xslt";
    private static final String XMLForm_Inserted="/validation/input/sensorSML_Elements_Inserted.xml";
    private static final String sensorXML="/templates/sensor.xml";
	/*
	 * @BeforeClass public static void before() {
	 * mService.setMergeDocument(DOMUtil.createDocumentFromSystemID(
	 * "classpath:/test-sensor.xml", true)); }
	 */

	@BeforeClass
	public static void createValidator() throws ParserConfigurationException {
		validator = new SchematronValidator();
		validator.setRulessetSystemID(rulesetSystemID);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		documentBuilder = factory.newDocumentBuilder();
	}

	private String validateResource(String resourcePath) throws SAXException,
			IOException {
		Document lReport = DOMUtil.newDocument(true);
		Document input = documentBuilder.parse(TestValidationExtended.class
				.getResourceAsStream(resourcePath));

		Source lSource = new DOMSource(input);
		Result lResult = new DOMResult(lReport);
		xsltTransformer.setRulesetSystemID(validator.getRulesetSystemID());
		xsltTransformer.transform(lSource, lResult);

		String resultString = DOMUtil.convertToString(lReport, true);
		return resultString;
	}

//ExtendedValidation::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@Test
	public void testUniqueIDInserted() throws SAXException, IOException {
		String resultString = validateResource(XMLForm_Inserted);
		// System.out.println(resultString);
		assertThat(
				"Unique ID exists but is not recognized.",
				resultString,
				not(containsString("errors.validation.SML.extended.uniqueID")));
	}
	@Test
	public void testLongNameInserted() throws SAXException, IOException {
		String resultString = validateResource(XMLForm_Inserted);
		// System.out.println(resultString);
		assertThat(
				"Long name exists but is not recognized.",
				resultString,
				not(containsString("errors.validation.SML.extended.longName")));
	}
	@Test
	public void testShortNameInserted() throws SAXException, IOException {
		String resultString = validateResource(XMLForm_Inserted);
		// System.out.println(resultString);
		assertThat(
				"Short name exists but is not recognized.",
				resultString,
				not(containsString("errors.validation.SML.extended.shortName")));
	}
	@Test
	public void testKeywordInserted() throws SAXException, IOException {
		String resultString = validateResource(XMLForm_Inserted);
		// System.out.println(resultString);
		assertThat(
				"Keyword exists but is not recognized.",
				resultString,
				not(containsString("errors.validation.SML.extended.keyword")));
	}
	@Test
	public void testLongNameNotInserted() throws SAXException,
			IOException {
		String resultString = validateResource(sensorXML);
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing long name not identified ",
				resultString,
				containsString("errors.validation.SML.extended.longName"));
	}


	@Test
	public void testShortNameNotInserted() throws SAXException,
			IOException {
		String resultString = validateResource(sensorXML);
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing short name  not identified ",
				resultString,
				containsString("errors.validation.SML.extended.shortName"));
	}


	@Test
	public void testUniqueIDNotInserted() throws SAXException,
			IOException {
		String resultString = validateResource(sensorXML);
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing unique ID not identified ",
				resultString,
				containsString("errors.validation.SML.extended.uniqueID"));
	}


	@Test
	public void testKeywordNotInserted() throws SAXException,
			IOException {
		String resultString = validateResource(sensorXML);
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing keyword not identified ",
				resultString,
				containsString("errors.validation.SML.extended.keyword"));
	}
}
