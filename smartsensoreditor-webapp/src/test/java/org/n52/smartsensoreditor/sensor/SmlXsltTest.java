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
	
	/**
	  This method tests, if the classifierList element is not inserted, when no classifiers are inserted
	 */
	@Test
	public void testSweCharacteristicListNotExists() {
	
		Document doc = beanTransformerService.mergeToISO(multiSweQuantityCharacteristic,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					is(not(hasXPath(
							"/*/sml:characteristics/sml:CharacteristicList",
							usingNamespaces))));
		
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	/**
	  This method tests, if the test-value for smlIdentification is copied into the xml document.
	 */
	@Test
	public void testSweQuantityCharacteristic() {
	
		BeanUtil.setProperty(sweQuantityCharacteristic, "identifier","testIdentifier");
		BeanUtil.setProperty(sweQuantityCharacteristic, "label","testLabel");
		BeanUtil.setProperty(sweQuantityCharacteristic, "description","testDescription");
		BeanUtil.setProperty(sweQuantityCharacteristic, "uom","testUom");
		BeanUtil.setProperty(sweQuantityCharacteristic, "value","20.2");
		BeanUtil.setProperty(sweQuantityCharacteristic, "definition","testDefinition");
		
		multiSweQuantityCharacteristic.getItems().add(sweQuantityCharacteristic);
		Document doc = beanTransformerService.mergeToISO(multiSweQuantityCharacteristic,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {

			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity[@definition='testDefinition']/swe:identifier[text()='testIdentifier']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:label[text()='testLabel']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:description[text()='testDescription']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:value[text()='20.2']",
							usingNamespaces));
			assertThat(
					beanSource,
					hasXPath(
							"/*/sml:characteristics/sml:CharacteristicList/sml:characteristic/swe:Quantity/swe:uom[@code='testUom']",
							usingNamespaces));
			
			
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	
	/**
	  This method tests, if the test-value for smlCapabilityText is copied into the xml document.
	 */
	@Test
	public void testSmlCapabilityText() {
	
		BeanUtil.setProperty(smlCapabilityText, "capabilityName","testName");
		BeanUtil.setProperty(smlCapabilityText, "definition","testDefinition");
		BeanUtil.setProperty(smlCapabilityText, "label","testLabel");
		BeanUtil.setProperty(smlCapabilityText, "constraintValue","testValue1  , testValue2");
		BeanUtil.setProperty(smlCapabilityText, "constraintPatterns","testPattern");
		BeanUtil.setProperty(smlCapabilityText, "value","testValue");
		
		multiSmlCapabilityText.getItems().add(smlCapabilityText);
		Document doc = beanTransformerService.mergeToISO(multiSmlCapabilityText,mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {

			assertThat("test capabilityName",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName']",
							usingNamespaces));
			assertThat("test definition",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName']/swe:Text[@definition='testDefinition']",
							usingNamespaces));
			assertThat("test label",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName']/swe:Text[@definition='testDefinition']/swe:label[text()='testLabel']",
							usingNamespaces));
			assertThat("test constraintValue",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName']/swe:Text[@definition='testDefinition']/swe:constraint/swe:AllowedTokens/swe:value[text()='testValue2']",
							usingNamespaces));
			assertThat("test constraintPattern",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName']/swe:Text[@definition='testDefinition']/swe:constraint/swe:AllowedTokens/swe:pattern[text()='testPattern']",
							usingNamespaces));
			assertThat("test value",
					beanSource,
					hasXPath(
							"/*/sml:capabilities/sml:CapabilityList/sml:capability[@name='testName']/swe:Text[@definition='testDefinition']/swe:value[text()='testValue']",
							usingNamespaces));
			
			
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
