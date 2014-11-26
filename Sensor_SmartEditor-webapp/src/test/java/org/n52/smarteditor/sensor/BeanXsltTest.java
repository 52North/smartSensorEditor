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

	Document mDatasetDocument = DOMUtil.createFromStream(
			BeanXsltTest.class.getResourceAsStream("/sensor.xml"), true);

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

}
