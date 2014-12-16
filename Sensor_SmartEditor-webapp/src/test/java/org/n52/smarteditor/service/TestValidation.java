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



/*
 * Notice: testKeywordListNOTExistsComponent and testKeywordListExistsComponent not implemented
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/sml-BackendManagerService-config.xml")
public class TestValidation {
	@Resource(name = "xsltTransformerService")
	XSLTTransformerService xsltTransformer;

	/*
	 * @Resource(name = "backendSetupService") BackendManagerService mService;
	 */

	private static SchematronValidator validator;

	private static DocumentBuilder documentBuilder;

	private static final String rulesetSystemID = "/validation/SensorML_Profile_for_Discovery.xslt";

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
		Document input = documentBuilder.parse(TestValidation.class
				.getResourceAsStream(resourcePath));

		Source lSource = new DOMSource(input);
		Result lResult = new DOMResult(lReport);
		xsltTransformer.setRulesetSystemID(validator.getRulesetSystemID());
		xsltTransformer.transform(lSource, lResult);

		String resultString = DOMUtil.convertToString(lReport, true);
		return resultString;
	}

	/*
	 * @Test public void testMergeBackend() {
	 * 
	 * String mergedDocument = DOMUtil.convertToString( mService.mergeBackend(),
	 * false); Document lReport = mService.validate("smlValidator"); String
	 * report = DOMUtil.convertToString(lReport, true);
	 * System.out.println("Report" + report); }
	 */
	@Test
	public void testOneMemberExists() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: member not recognized",
				resultString,
				not(containsString("<svrl:text>errors.validation.SML.member</svrl:text>")));
	}

	@Test
	public void testOneMemberNOTExistsOrMoreThanOne() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noMember.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing member not identified ",
				resultString,
				containsString("<svrl:text>errors.validation.SML.member</svrl:text>"));
		 resultString = validateResource("/validation/input/manyMembers.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: too many members not identified ",
				resultString,
				containsString("<svrl:text>errors.validation.SML.member</svrl:text>"));

	}
	
	@Test
	public void testOneSystemExists() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: system not recognized",
				resultString,
				not(containsString("<svrl:text>errors.validation.SML.system</svrl:text>")));
	}

	@Test
	public void testOneSystemNOTExistsOrMoreThanOne() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noSystem.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing system not identified ",
				resultString,
				containsString("<svrl:text>errors.validation.SML.system</svrl:text>"));
		 resultString = validateResource("/validation/input/manySystems.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: too many systems not identified ",
				resultString,
				containsString("errors.validation.SML.system"));

	}
	@Test
	public void testKeywordListExistsSystem() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: keywordList not recognized",
				resultString,
				not(containsString("errors.validation.SML.KeywordList")));
	}

	@Test
	public void testKeywordListNOTExistsSystem() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noKeywordList.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing keywordList not identified ",
				resultString,
				containsString("errors.validation.SML.KeywordList"));
	}
	@Test
	public void testDefinitionInTermExists() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: definition in one or more Term elements within //sml:identification/sml:IdentifierList/sml:identifier/sml:Term  not recognized",
				resultString,
				not(containsString("errors.validation.SML.identification.definition")));
	}

	@Test
	public void testDefinitionInTermNotExists() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noDefinitionInTerm.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing definition in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term  not identified ",
				resultString,
				containsString("errors.validation.SML.identification.definition"));
	}
	@Test
	public void testDefinitionLongNameExists() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: definition urn:ogc:def:identifier:OGC:1.0:longName in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term not recognized",
				resultString,
				not(containsString("errors.validation.SML.identification.longName")));
	}

	@Test
	public void testDefinitionLongNameNotExists() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noDefinitionLongName.xml");
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing definition urn:ogc:def:identifier:OGC:1.0:longName in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term  not identified ",
				resultString,
				containsString("errors.validation.SML.identification.longName"));
	}
	@Test
	public void testDefinitionShortNameExists() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: definition urn:ogc:def:identifier:OGC:1.0:shortName in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term not recognized",
				resultString,
				not(containsString("errors.validation.SML.identification.shortName")));
	}

	@Test
	public void testDefinitionShortNameNotExists() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noDefinitionShortName.xml");
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing definition urn:ogc:def:identifier:OGC:1.0:shortName in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term  not identified ",
				resultString,
				containsString("errors.validation.SML.identification.shortName"));
	}
	@Test
	public void testDefinitionUniqueIDExists() throws SAXException, IOException {
		String resultString = validateResource("/templates/sensor.xml");
		// System.out.println(resultString);
		assertThat(
				"Validation incorrect: definition urn:ogc:def:identifier:OGC:1.0:uniqueID in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term not recognized",
				resultString,
				not(containsString("errors.validation.SML.identification.uniqueID")));
	}

	@Test
	public void testDefinitionUniqueIDNotExists() throws SAXException,
			IOException {
		String resultString = validateResource("/validation/input/noUniqueID.xml");
		 //System.out.println(resultString);
		assertThat(
				"Validation incorrect: missing definition urn:ogc:def:identifier:OGC:1.0:uniqueID in //sml:identification/sml:IdentifierList/sml:identifier/sml:Term  not identified ",
				resultString,
				containsString("errors.validation.SML.identification.uniqueID"));
	}
	/*
	 * @Test public void testTransformation() {
	 * 
	 * String formattedOutput = ""; try {
	 * 
	 * TransformerFactory tFactory = TransformerFactory.newInstance();
	 * Transformer transformer = tFactory.newTransformer( new StreamSource(
	 * "//target//test-classes//validation//iso_svrl_for_xslt2TEST.xsl") );
	 * 
	 * StreamSource xmlSource = new StreamSource(new
	 * StringReader("//target//test-classes//test-sensor.xml") );
	 * ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 * transformer.transform( xmlSource, new StreamResult( baos ) );
	 * 
	 * formattedOutput = baos.toString();
	 * 
	 * } catch( Exception e ) { e.printStackTrace(); } XSLTTransformerService
	 * transform=new XSLTTransformerService(); Document lResultDoc =
	 * DOMUtil.newDocument(true); Document lLocalDoc =
	 * DOMUtil.createDocumentFromSystemID("/templates/sensor.xml", true); Source
	 * lSource = new DOMSource(lLocalDoc); Result lResult = new
	 * DOMResult(lResultDoc); ValidatorService toXslt=new ValidatorService();
	 * toXslt.setPath("/validation"); toXslt.setSvrlFilename("");
	 * toXslt.setXsltService(transform); toXslt.compile();
	 * 
	 * }
	 */

}
