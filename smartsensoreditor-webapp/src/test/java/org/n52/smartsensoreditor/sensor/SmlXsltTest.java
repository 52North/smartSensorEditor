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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;
import static org.xmlmatchers.XmlMatchers.hasXPath;
import static org.xmlmatchers.xpath.XpathReturnType.returningANumber;
import static org.hamcrest.Matchers.equalTo;



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
@ContextConfiguration(locations = {"/sml-transformer-config.xml"})
public class SmlXsltTest {
	static private Logger LOG = Logger.getRootLogger();
	SimpleNamespaceContext usingNamespaces;
	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	Document mRefDatasetDocument = DOMUtil.createFromStream(
			SmlXsltTest.class.getResourceAsStream("/validation/input/testBeanTOSmlXSLT.xml"), true);


	@Resource(name = "smlKeyword")
	BaseBean smlKeyword;

	@Resource(name = "smlIdentification")
	BaseBean smlIdentification;
	@Resource(name = "smlClassification")
	BaseBean smlClassification;

	@Resource(name = "smlIdentifier")
	BaseBean smlIdentifier;


	@Resource(name = "sweQuantityCharacteristic")
	BaseBean sweQuantityCharacteristic;

	@Resource(name = "smlCapabilityText")
	BaseBean smlCapabilityText;

	@Resource(name = "smlCapabilityText2")
	BaseBean smlCapabilityText2;

	@Resource(name = "smlCapabilityText3")
	BaseBean smlCapabilityText3;

	@Resource(name = "smlCapabilityText4")
	BaseBean smlCapabilityText4;
	
	@Resource(name = "smlCapabilityText5")
	BaseBean smlCapabilityText5;

	@Resource(name = "multiSmlKeyword")
	MultipleElementBean multiSmlKeyword;

	@Resource(name = "multiSmlIdentification")
	MultipleElementBean multiSmlIdentification;

	@Resource(name = "multiSmlClassification")
	MultipleElementBean multiSmlClassification;


	@Resource(name = "multiSweQuantityCharacteristic")
	MultipleElementBean multiSweQuantityCharacteristic;

	@Resource(name = "multiSmlCapabilityText")
	MultipleElementBean multiSmlCapabilityText;

	@Before
	public void before() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("sml", "http://www.opengis.net/sensorml/2.0");
		map.put("swe", "http://www.opengis.net/swe/2.0");
		map.put("gml", "http://www.opengis.net/gml/3.2");
		map.put("xlink", "http://www.w3.org/1999/xlink");
		usingNamespaces = new SimpleNamespaceContext();
		usingNamespaces.setBindings(map);
	}

	/**
	  This method tests, if the test-value for keyword is copied into the xml document.
	 */
	@Test
	public void testKeyword() {

		BeanUtil.setProperty(smlKeyword, "keyword","testkeyword");
		multiSmlKeyword.getItems().add(smlKeyword);
		Document doc = beanTransformerService.mergeToISO(multiSmlKeyword,mRefDatasetDocument);
		multiSmlKeyword.getItems().remove(smlKeyword);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:keywords/sml:KeywordList/sml:keyword[text()='testkeyword']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the test-value for smlIdentification is copied into the xml document.
	 */
	@Test
	public void testSmlIdentification() {
		BeanUtil.setProperty(smlIdentification, "definition","testdefinition");
		BeanUtil.setProperty(smlIdentification, "label","testname");
		BeanUtil.setProperty(smlIdentification, "codeSpace","http://testIdentifierCodeSpace");
		BeanUtil.setProperty(smlIdentification, "value","testvalue");
		multiSmlIdentification.getItems().add(smlIdentification);
		Document doc = beanTransformerService.mergeToISO(multiSmlIdentification,mRefDatasetDocument);
		multiSmlIdentification.getItems().remove(smlIdentification);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='testdefinition']/sml:value[text()='testvalue']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='testdefinition']/sml:label[text()='testname']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='testdefinition']/sml:codeSpace[@xlink:href='http://testIdentifierCodeSpace']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the test-value for smlClassification is copied into the xml document.
	 */
	@Test
	public void testSmlClassification() {
		BeanUtil.setProperty(smlClassification, "definition","testdefinition");
		BeanUtil.setProperty(smlClassification, "label","testname");
		BeanUtil.setProperty(smlClassification, "codeSpace","http://testClassifierCodeSpace");
		BeanUtil.setProperty(smlClassification, "value","testvalue");
		multiSmlClassification.getItems().add(smlClassification);
		Document doc = beanTransformerService.mergeToISO(multiSmlClassification,mRefDatasetDocument);
		multiSmlClassification.getItems().remove(smlClassification);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:classification/sml:ClassifierList/sml:classifier/sml:Term[@definition='testdefinition']/sml:value[text()='testvalue']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:classification/sml:ClassifierList/sml:classifier/sml:Term[@definition='testdefinition']/sml:label[text()='testname']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:classification/sml:ClassifierList/sml:classifier/sml:Term[@definition='testdefinition']/sml:codeSpace[@xlink:href='http://testClassifierCodeSpace']",
							usingNamespaces));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}

	/**
	  This method tests, if the classifierList element is not inserted, when no classifiers are inserted
	 */
	@Test
	public void testSmlClassifierListNotExists() {

		Document doc = beanTransformerService.mergeToISO(multiSmlClassification,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					is(not(hasXPath(
							"/*/sml:classification/sml:ClassifierList",
							usingNamespaces))));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the classifierList element is not inserted, when no classifiers are inserted
	 */
	@Test
	public void testSmlIdentifierListNotExists() {

		Document doc = beanTransformerService.mergeToISO(multiSmlIdentification,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					is(not(hasXPath(
							"/*/sml:identification/sml:IdentifierList",
							usingNamespaces))));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the keywordList element is not inserted, when no keywords are inserted
	 */
	@Test
	public void testSmlKeywordListNotExists() {

		Document doc = beanTransformerService.mergeToISO(multiSmlKeyword,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					is(not(hasXPath(
							"/*/sml:keywords/sml:KeywordList",
							usingNamespaces))));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}

//	/**
//	  This method tests, if the classifierList element is not inserted, when no classifiers are inserted
//	 */
//	@Test
//	public void testSweCharacteristicListNotExists() {
//
//		Document doc = beanTransformerService.mergeToISO(multiSweQuantityCharacteristic,mRefDatasetDocument);
//		Source beanSource = new DOMSource(doc);
//		try {
//			assertThat(
//					beanSource,
//					is(not(hasXPath(
//							"/*/sml:characteristics/sml:CharacteristicList",
//							usingNamespaces))));
//
//		} catch (NoSuchMethodError e) {
//			LOG.error("Possibly XPath is invalid with compared source", e);
//			throw e;
//		}
//
//	}
//	/**
//	  This method tests, if the test-value for smlIdentification is copied into the xml document.
//	 */
//	@Test
//	public void testSweQuantityCharacteristic() {
//
//		BeanUtil.setProperty(sweQuantityCharacteristic, "identifier","testIdentifier");
//		BeanUtil.setProperty(sweQuantityCharacteristic, "label","testLabel");
//		BeanUtil.setProperty(sweQuantityCharacteristic, "description","testDescription");
//		BeanUtil.setProperty(sweQuantityCharacteristic, "uom","testUom");
//		BeanUtil.setProperty(sweQuantityCharacteristic, "value","20.2");
//		BeanUtil.setProperty(sweQuantityCharacteristic, "definition","testDefinition");
//
//		multiSweQuantityCharacteristic.getItems().add(sweQuantityCharacteristic);
//		Document doc = beanTransformerService.mergeToISO(multiSweQuantityCharacteristic,mRefDatasetDocument);
//		multiSweQuantityCharacteristic.getItems().remove(sweQuantityCharacteristic);	
//		Source beanSource = new DOMSource(doc);
//		try {
//
//			assertThat(
//					beanSource,
//					hasXPath(
//							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity[@definition='testDefinition']/swe:identifier[text()='testIdentifier']",
//							usingNamespaces));
//			assertThat(
//					beanSource,
//					hasXPath(
//							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:label[text()='testLabel']",
//							usingNamespaces));
//			assertThat(
//					beanSource,
//					hasXPath(
//							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:description[text()='testDescription']",
//							usingNamespaces));
//			assertThat(
//					beanSource,
//					hasXPath(
//							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:value[text()='20.2']",
//							usingNamespaces));
//			assertThat(
//					beanSource,
//					hasXPath(
//							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:uom[@code='testUom']",
//							usingNamespaces));
//
//
//		} catch (NoSuchMethodError e) {
//			LOG.error("Possibly XPath is invalid with compared source", e);
//			throw e;
//		}
//
//	}


	/**
	  This method tests, if the test-value for smlCapabilityText is copied into the xml document.
	 */
	@Test
	public void testSmlCapabilityText() {
		BeanUtil.setProperty(smlCapabilityText, "capabilitiesName","TestCapabilitiesName1");
		BeanUtil.setProperty(smlCapabilityText, "capabilityName","testName1");
		BeanUtil.setProperty(smlCapabilityText, "definition","testDefinition1");
		BeanUtil.setProperty(smlCapabilityText, "label","testLabel1");
		BeanUtil.setProperty(smlCapabilityText, "constraintValue","testValue0  , testValue00");
		BeanUtil.setProperty(smlCapabilityText, "constraintPatterns","testPattern1");
		BeanUtil.setProperty(smlCapabilityText, "value","testValue1");

		multiSmlCapabilityText.getItems().add(smlCapabilityText);

		BeanUtil.setProperty(smlCapabilityText2, "capabilitiesName","TestCapabilitiesName2");
		BeanUtil.setProperty(smlCapabilityText2, "capabilityName","testName2");
		BeanUtil.setProperty(smlCapabilityText2, "definition","testDefinition2");
		BeanUtil.setProperty(smlCapabilityText2, "label","testLabel2");
		BeanUtil.setProperty(smlCapabilityText2, "constraintValue","testValue00  , testValue00");
		BeanUtil.setProperty(smlCapabilityText2, "constraintPatterns","testPattern2");
		BeanUtil.setProperty(smlCapabilityText2, "value","testValue2");

		multiSmlCapabilityText.getItems().add(smlCapabilityText2);

		BeanUtil.setProperty(smlCapabilityText3, "capabilitiesName","TestCapabilitiesName2");
		BeanUtil.setProperty(smlCapabilityText3, "capabilityName","testName3");
		BeanUtil.setProperty(smlCapabilityText3, "definition","testDefinition3");
		BeanUtil.setProperty(smlCapabilityText3, "label","testLabel3");
		BeanUtil.setProperty(smlCapabilityText3, "constraintValue","testValue00  , testValue00");
		BeanUtil.setProperty(smlCapabilityText3, "constraintPatterns","testPattern3");
		BeanUtil.setProperty(smlCapabilityText3, "value","testValue3");

		multiSmlCapabilityText.getItems().add(smlCapabilityText3);

		BeanUtil.setProperty(smlCapabilityText4, "capabilitiesName","TestCapabilitiesName1");
		BeanUtil.setProperty(smlCapabilityText4, "capabilityName","testName4");
		BeanUtil.setProperty(smlCapabilityText4, "definition","testDefinition4");
		BeanUtil.setProperty(smlCapabilityText4, "label","testLabel4");
		BeanUtil.setProperty(smlCapabilityText4, "constraintValue","testValue00  , testValue00");
		BeanUtil.setProperty(smlCapabilityText4, "constraintPatterns","testPattern4");
		BeanUtil.setProperty(smlCapabilityText4, "value","testValue4");

		multiSmlCapabilityText.getItems().add(smlCapabilityText4);
		
		BeanUtil.setProperty(smlCapabilityText5, "capabilitiesName","Test5");
		BeanUtil.setProperty(smlCapabilityText5, "capabilityName","testName5");
		BeanUtil.setProperty(smlCapabilityText5, "definition","testDefinition5");
		BeanUtil.setProperty(smlCapabilityText5, "label","testLabel5");
		BeanUtil.setProperty(smlCapabilityText5, "constraintValue","testValue00  , testValue00");
		BeanUtil.setProperty(smlCapabilityText5, "constraintPatterns","testPattern5");
		BeanUtil.setProperty(smlCapabilityText5, "value","testValue5");

		multiSmlCapabilityText.getItems().add(smlCapabilityText5);
		Document  doc = beanTransformerService.mergeToISO(multiSmlCapabilityText,mRefDatasetDocument);
		multiSmlCapabilityText.getItems().remove(smlCapabilityText);
		multiSmlCapabilityText.getItems().remove(smlCapabilityText2);
		multiSmlCapabilityText.getItems().remove(smlCapabilityText3);
		multiSmlCapabilityText.getItems().remove(smlCapabilityText4);
		multiSmlCapabilityText.getItems().remove(smlCapabilityText5);
		Source beanSource = new DOMSource(doc);
		try {
			//test if values are inserted
			assertThat("test capabilityName",
					beanSource,
					hasXPath(
							"/*/sml:capabilities[@name='TestCapabilitiesName1']",
							usingNamespaces));
			assertThat("test capabilityName",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName1']",
							usingNamespaces));
			assertThat("test definition",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName1']/swe:Text[@definition='testDefinition1']",
							usingNamespaces));
			assertThat("test label",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName1']/swe:Text[@definition='testDefinition1']/swe:label[text()='testLabel1']",
							usingNamespaces));
			assertThat("test constraintValue",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName1']/swe:Text[@definition='testDefinition1']/swe:constraint/swe:AllowedTokens/swe:value[text()='testValue00']",
							usingNamespaces));
			assertThat("test constraintPattern",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName1']/swe:Text[@definition='testDefinition1']/swe:constraint/swe:AllowedTokens/swe:pattern[text()='testPattern1']",
							usingNamespaces));
			assertThat("test value",
					beanSource,
					hasXPath(
							"/sml:PhysicalSystem/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName1']/swe:Text[@definition='testDefinition1']/swe:value[text()='testValue1']",
							usingNamespaces));
			//test capabilities with name=TestCapabilitiesName1 exists exact 1 time.
			assertThat("test one capability with name=TestCapabilitiesName1 ",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1'])",
							usingNamespaces,equalTo("1")));

			//test if testName3 is inserted into TestCapabilitiesName2
			assertThat("test if testName3 is inserted into TestCapabilitiesName2",
					beanSource,
					hasXPath("/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName2']/sml:CapabilityList/sml:capability[@name='testName3']/swe:Text[@definition='testDefinition3']/swe:value[text()='testValue3']",
							usingNamespaces));

			//test if testName4 is inserted into TestCapabilitiesName1
			assertThat("test if testName3 is inserted into TestCapabilitiesName1",
					beanSource,
					hasXPath("/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1']/sml:CapabilityList/sml:capability[@name='testName4']/swe:Text[@definition='testDefinition4']/swe:value[text()='testValue4']",
							usingNamespaces));

			//delete all capabilities that have only text nodes
			assertThat("delete all capabilities that have only text nodes",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='xxx'])",
							usingNamespaces,equalTo("0")));
			//has no empty capability Lists
			assertThat("delete all capabilities that have no nodes",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='yyy'])",
							usingNamespaces,equalTo("0")));
			//has no empty capability Lists
			assertThat("has no empty capability Lists",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities/sml:CapabilityList[not(*)])",
							usingNamespaces,equalTo("0")));
			//insert into Test5
			assertThat("has no empty capability Lists",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='Test5']/sml:CapabilityList/sml:capability[@name='testName5']/swe:Text[@definition='testDefinition5']/swe:value[text()='testValue5'])",
							usingNamespaces,equalTo("1")));
			//Test5 has element sml:test/text()=testen
			assertThat("has no empty capability Lists",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='Test5']/sml:CapabilityList/sml:test[text()='testen'])",
							usingNamespaces,equalTo("1")));
			//Test6 has element sml:test/text()=testen
			assertThat("has no empty capability Lists",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='Test6']/sml:CapabilityList/sml:test[text()='testen'])",
							usingNamespaces,equalTo("1")));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the SmlCapabilityText element is not inserted, when no capabilities are inserted
	 */
	@Test
	public void testSmlCapabilityTextNotExists() {
		Document  doc = beanTransformerService.mergeToISO(multiSmlCapabilityText,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					is(not(hasXPath(
							"/sml:PhysicalSystem/sml:capabilities/sml:CapabilityList/sml:capability/swe:Text",
							usingNamespaces))));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	
	/**
	  This method tests, if not required empty fields are not inserted into the sml document
	 */
	@Test
	public void testSmlCapabilityTextNotInsertFields() {
		BeanUtil.setProperty(smlCapabilityText, "capabilitiesName","TestCapabilitiesName1");
		BeanUtil.setProperty(smlCapabilityText, "capabilityName","testName1");
		BeanUtil.setProperty(smlCapabilityText, "definition","testDefinition1");
		BeanUtil.setProperty(smlCapabilityText, "label","");
		BeanUtil.setProperty(smlCapabilityText, "constraintValue","");
		BeanUtil.setProperty(smlCapabilityText, "constraintPatterns","");
		BeanUtil.setProperty(smlCapabilityText, "value","");
		
		multiSmlCapabilityText.getItems().add(smlCapabilityText);
		
		Document  doc = beanTransformerService.mergeToISO(multiSmlCapabilityText,mRefDatasetDocument);
		multiSmlCapabilityText.getItems().remove(smlCapabilityText);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat("test label",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1']/sml:CapabilityList/sml:capability[@name='testName5']/swe:Text[@definition='testDefinition1']/swe:label)",
							usingNamespaces,equalTo("0")));
			assertThat("test constraintValue",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1']/sml:CapabilityList/sml:capability[@name='testName5']/swe:Text[@definition='testDefinition1']/swe:constraint/swe:AllowedTokens/swe:value)",
							usingNamespaces,equalTo("0")));
			assertThat("test constraintPattern",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1']/sml:CapabilityList/sml:capability[@name='testName5']/swe:Text[@definition='testDefinition1']/swe:constraint/swe:AllowedTokens/swe:pattern)",
							usingNamespaces,equalTo("0")));
			assertThat("test constraintPattern",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1']/sml:CapabilityList/sml:capability[@name='testName5']/swe:Text[@definition='testDefinition1']/swe:value)",
							usingNamespaces,equalTo("0")));
			assertThat("test constraints not inserted",
					beanSource,
					hasXPath("count(/sml:PhysicalSystem/sml:capabilities[@name='TestCapabilitiesName1']/sml:CapabilityList/sml:capability[@name='testName5']/swe:Text[@definition='testDefinition1']/swe:constraint)",
							usingNamespaces,equalTo("0")));

		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the test-value for uniqueId is copied into the xml document.
	 */
	@Test
	public void testIdentifer() {
		BeanUtil.setProperty(smlIdentifier, "id", "testIdentifier");

		Document doc = beanTransformerService.mergeToISO(smlIdentifier,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//gml:identifier[text()='testIdentifier']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
}
