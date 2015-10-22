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

package org.n52.smartsensoreditor.service;

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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.arrayWithSize;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/sml-BackendManagerService-config.xml")
public class TestValidation {
	@Resource(name = "xsltTransformerService")
	XSLTTransformerService xsltTransformer;


	private static SchematronValidator validator;

	private static DocumentBuilder documentBuilder;

	private static final String rulesetSystemID = "/validation/SensorML_Profile_for_Discovery.xslt";
	private static final String sensorXML="/templates/sensor.xml";


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
		Document input = documentBuilder.parse(TestValidation.class
				.getResourceAsStream(resourcePath));

		Source lSource = new DOMSource(input);
		Result lResult = new DOMResult(lReport);
		xsltTransformer.setRulesetSystemID(validator.getRulesetSystemID());
		xsltTransformer.transform(lSource, lResult);

		String resultString = DOMUtil.convertToString(lReport, true);
		return resultString;
	}

	@Test
	public void testNoValidationErrors() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/noErrors.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: Errors are thrown although the file has no errors",
				resultString,
				not(containsString("error")));
	}
	
	@Test
	public void testReq40() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_Req40.xml");
		assertThat(
				"Validation incorrect: Req 40 element with no childs and attributes not identified.",
				resultString,
				containsString("errors.validation.SML.2.0.xlink_href"));
	}
	@Test
	public void testReq40_noerrors() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/no_errors_Req40.xml");

		assertThat(
				"Validation incorrect: Req 40 element with no childs and attributes not identified.",
				resultString,
				not(containsString("errors.validation.SML.2.0.xlink_href")));
	}
	@Test
	public void testPhysicalSystem() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_physicalSystem.xml");

		assertThat(
				"Validation incorrect: gml identifer exists multiple times error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.gmlIdentifier"));
		assertThat(
				"Validation incorrect: gml identifer has no codeSpace attribute with value 'uniqueId' error",
				resultString,
				containsString("errors.validation.SML.2.0.gmlIdentifier.uniqueID"));
	}
	@Test
	public void testPhysicalSystem_noGmlIdentifier() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_physicalSystem_noGmlIdentifier.xml");

		assertThat(
				"Validation incorrect: gml identifer not exists error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.gmlIdentifier"));

	}
	@Test
	public void testGmlDescription() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_gmlDescription.xml");

		assertThat(
				"Validation incorrect: gml description not exists error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.gmlDescription"));

	}
	@Test
	public void testSmlKeywords() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_keywords.xml");

		assertThat(
				"Validation incorrect: sml keywordlist does not exists within keyword element error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlKeywordList"));

	}
	@Test
	public void testSmlIdentification() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_identification.xml");

		assertThat(
				"Validation incorrect: definition attribute has length ==0 error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlIdentification.definition"));
		String[] list=resultString.split("errors.validation.SML.2.0.discovery.smlIdentification.definition");
		assertThat(
				"Validation incorrect: the error errors.validation.SML.2.0.discovery.smlIdentification.definition should be thrown 2 times",
				list,
				arrayWithSize(equalTo(3)));
		assertThat(
				"Validation incorrect: @definition = 'http://www.nexosproject.eu/dictionary/definitions.html#UUID' error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlIdentification.UUID"));
		assertThat(
				"Validation incorrect: @definition = 'http://www.nexosproject.eu/dictionary/definitions.html#longName error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlIdentification.longName"));
		assertThat(
				"Validation incorrect: @definition = 'http://www.nexosproject.eu/dictionary/definitions.html#shortName' error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlIdentification.shortName"));

	}
	@Test
	public void testSmlClassification() throws SAXException, IOException {
		String resultString = validateResource("/validation/input/errors_classification.xml");

		assertThat(
				"Validation incorrect: definition attribute has length ==0 error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlClassification.definition"));
		String[] list=resultString.split("errors.validation.SML.2.0.discovery.smlClassification.definition");
		assertThat(
				"Validation incorrect: the error errors.validation.SML.2.0.discovery.smlClassification.definition should be thrown 2 times",
				list,
				arrayWithSize(equalTo(5)));
		assertThat(
				"Validation incorrect: @definition =  'http://www.nexosproject.eu/dictionary/definitions.html#sensorType' error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlClassification.definition.sensorType"));
		assertThat(
				"Validation incorrect: @definition =  'http://www.nexosproject.eu/dictionary/definitions.html#intendedApplication' error not identified",
				resultString,
				containsString("errors.validation.SML.2.0.discovery.smlClassification.definition.intendedApplication"));


	}

	


	

}
