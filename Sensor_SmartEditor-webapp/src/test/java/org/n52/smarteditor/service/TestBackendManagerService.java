package org.n52.smarteditor.service;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import de.conterra.smarteditor.beans.BaseBean;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.hamcrest.Matchers.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/sml-BackendManagerService-config.xml")
public class TestBackendManagerService {
	@Resource(name = "backendSetupService")
	BackendManagerService backendManagerService;
	
	@Before
public void before(){
		backendManagerService.setMergeDocument(DOMUtil
				.createDocumentFromSystemID("classpath:/test-sensor.xml", true));
}
	/**
	 * Tests if all needed beans are used in mergeBackend. If that is true all 
	 * values "test" have to be overwritten by the beans.
	 */
	@Test
	public void testMergeBackend() {
		
		String docString1 = DOMUtil.convertToString(backendManagerService.getMergeDocument()
				, false);
		assertThat(docString1,containsString("test"));
	
		String docString2 = DOMUtil.convertToString(
				backendManagerService.mergeBackend(), false);
		assertThat(docString2, not(containsString("test")));
	}

	@Test
public void testRegexSML(){
		
	for (Map.Entry<String, BaseBean> lEntry : backendManagerService.getBackend().getStorage()
			.entrySet()) {
		// check if bean should be merged
		if (backendManagerService.isBeanActive(lEntry.getKey())) {
			 assertThat(lEntry.getKey(),containsString("sml"));
		}
	}	
	}
@Test 
public void testRegexNOSML(){
	backendManagerService.setMergeDocument(DOMUtil
			.createDocumentFromSystemID("classpath:/dataset.xml", true));
	for (Map.Entry<String, BaseBean> lEntry : backendManagerService.getBackend().getStorage()
			.entrySet()) {
		// check if bean should be merged
		if (backendManagerService.isBeanActive(lEntry.getKey())) {
			 assertThat(lEntry.getKey(),not(containsString("sml")));
		}
	}
}
}
