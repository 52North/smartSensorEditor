package org.n52.smarteditor.service;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

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

	@Test
	public void testMergeBackend() {
		backendManagerService.setMergeDocument(DOMUtil
				.createDocumentFromSystemID("/templates/sensor.xml", true));
		Document doc = backendManagerService.mergeBackend();
		String docString = DOMUtil.convertToString(
				backendManagerService.mergeBackend(), false);
		assertThat(docString, containsString("longName"));
	}

	@Test 
public void testRegexSML(){
	backendManagerService.setMergeDocument(DOMUtil.createDocumentFromSystemID(
			"/templates/sensor.xml", true));
    String prefix="sml";
	if (!prefix.equals("")) {
		for (Map.Entry<String, BaseBean> lEntry : backendManagerService.getBackend().getStorage()
				.entrySet()) {
			String beanName = lEntry.getKey();
			if (beanName.matches(prefix + "\\w+")) {
				assertThat(beanName,containsString("sml"));
			}
		}
	} else {
		fail();
		Properties prop=backendManagerService.getActiveBeanNamesRegex();
		Collection<Object> prefixCollection=prop.values();
		String [] prefixArray=(String[]) prefixCollection.toArray(new String[0]);
		for (Map.Entry<String, BaseBean> lEntry : backendManagerService.getBackend().getStorage()
				.entrySet()) {
			String beanName = lEntry.getKey();
			for(String prefixElement :prefixArray){
			if (!prefixElement.equals("")&&!beanName.matches(prefixElement + "\\w+")) {
				
			}
			}
		}
	}
	}
@Test 
public void testRegexNOSML(){
	backendManagerService.setMergeDocument(DOMUtil.createDocumentFromSystemID(
			"/templates/sensor.xml", true));
    String prefix="";
	if (!prefix.equals("")) {fail();
		for (Map.Entry<String, BaseBean> lEntry : backendManagerService.getBackend().getStorage()
				.entrySet()) {
			String beanName = lEntry.getKey();
			if (beanName.matches(prefix + "\\w+")) {
				assertThat(beanName,containsString("sml"));
			}
		}
	} else {
	
		Properties prop=backendManagerService.getActiveBeanNamesRegex();
		Collection<Object> prefixCollection=prop.values();
		String [] prefixArray=(String[]) prefixCollection.toArray(new String[0]);
		for (Map.Entry<String, BaseBean> lEntry : backendManagerService.getBackend().getStorage()
				.entrySet()) {
			String beanName = lEntry.getKey();
			for(String prefixElement :prefixArray){
			if (!prefixElement.equals("")&&!beanName.matches(prefixElement + "\\w+")) {
				assertThat(beanName,not(containsString("sml")));
			}
			}
		}
	}
}
}
