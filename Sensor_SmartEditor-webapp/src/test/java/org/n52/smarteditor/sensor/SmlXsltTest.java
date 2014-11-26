package org.n52.smarteditor.sensor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.hasXPath;

import javax.annotation.Resource;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xmlmatchers.namespace.SimpleNamespaceContext;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import de.conterra.smarteditor.beans.BaseBean;
import de.conterra.smarteditor.service.BeanTransformerException;
import de.conterra.smarteditor.service.BeanTransformerService;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.beans.CodeListBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/sml-transformer-config.xml")
public class SmlXsltTest {
	static private Logger LOG = Logger.getRootLogger();
	NamespaceContext usingNamespaces;
	XStream mXStream;
	@Resource(name = "beanTransformerService")
	BeanTransformerService beanTransformerService;

	Document mRefDatasetDocument = DOMUtil.createFromStream(
			SmlXsltTest.class.getResourceAsStream("/sensor.xml"), true);
	@Resource(name = "smlLongName")
	BaseBean smlLongName;

	@Before
	public void before() {
		mXStream = new XStream(new StaxDriver());
		usingNamespaces = new SimpleNamespaceContext().withBinding("sml",
				"http://www.opengis.net/sensorML/1.0");
	}

	@Test
	public void testLongName() {
		mXStream.processAnnotations(smlLongName.getClass());
		mXStream.setClassLoader(smlLongName.getClass().getClassLoader());
		BaseBean lBean = (BaseBean) mXStream
				.fromXML("<SmlLongName><longName>testname</longName></SmlLongName>");

		lBean.setTransformToBean("/xslt/SmlLongName_bean.xslt");
		lBean.setTransformToISO("/xslt/SmlLongName_sml.xslt");
		Document bean = beanTransformerService.mergeToISO(lBean,
				mRefDatasetDocument);
		Source beanSource = new DOMSource(bean);
		
		try {
			assertThat(
					beanSource,
					hasXPath("//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:longName']/sml:value[text()='testname']",
							usingNamespaces));
		} catch (NoSuchMethodError e) {
			LOG.error("Possibly XPath is invalid with compared source", e);
			throw e;
		}

	}
}
