package org.n52.smarteditor.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.hasXPath;

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

import de.conterra.smarteditor.service.BeanTransformerService;
import de.conterra.smarteditor.util.DOMUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/sml-transformer-config.xml")
public class SmlXsltTest {
	static private Logger LOG = Logger.getRootLogger();
	NamespaceContext usingNamespaces;
	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	Document mRefDatasetDocument = DOMUtil.createFromStream(
			SmlXsltTest.class.getResourceAsStream("/sensor.xml"), true);

	@Resource(name = "smlLongName")
	BaseBean smlLongName;
	
	@Resource(name = "smlShortName")
	BaseBean smlShortName;

	@Resource(name = "smlUniqueID")
	BaseBean smlUniqueID;
	@Before
	public void before() {
		usingNamespaces = new SimpleNamespaceContext().withBinding("sml",
				"http://www.opengis.net/sensorML/1.0");
	}

	@Test
	public void testLongName() {
		BeanUtil.setProperty(smlLongName, "longName", "testname");
		Document doc = beanTransformerService.mergeToISO(smlLongName,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:longName']/sml:value[text()='testname']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	@Test
	public void testShortName() {
		BeanUtil.setProperty(smlShortName, "shortName", "shortName");
		Document doc = beanTransformerService.mergeToISO(smlShortName,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:shortName']/sml:value[text()='shortName']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
	@Test
	public void testUniqueID() {
		BeanUtil.setProperty(smlUniqueID, "uniqueID", "testunique");
		Document doc = beanTransformerService.mergeToISO(smlUniqueID,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(doc);
		try {
			assertThat(
					beanSource,
					hasXPath(
							"//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:uniqueID']/sml:value[text()='testunique']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
}
