package de.conterra.smarteditor.service;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import de.conterra.smarteditor.util.DOMUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/sml-BackendManagerService-config.xml")
public class TestBackendManagerService2 {

	@Resource(name = "backendSetupService")
	BackendManagerService mService;

	@Before
	public void before() {
		mService.setMergeDocument(DOMUtil.createDocumentFromSystemID(
				"classpath:/test-sensor.xml", false));
	}

	@Test
	public void testMergeBackend() {

		String mergedDocument = DOMUtil.convertToString(
				mService.mergeBackend(),false);
		Document lReport = mService.validate("smlValidator");
		String report = DOMUtil.convertToString(lReport, true);
	}

}
