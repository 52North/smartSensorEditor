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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.dom4j.dom.DOMDocument;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import de.conterra.smarteditor.beans.BackendBean;
import de.conterra.smarteditor.beans.BaseBean;
import de.conterra.smarteditor.beans.FileIdentifierBean;
import de.conterra.smarteditor.dao.LockManager;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.validator.SchematronValidator;
import de.conterra.smarteditor.xml.EditorContext;

/**
 * Service manages any access to and modification of the backend storage
 * <p/>
 *
 * @author kse
 * @author extended by Jana
 *         <p/>
 *         <p/>
 *         Date: 16.02.2010 Time: 15:19:00
 */
public class BackendManagerServiceSML extends BackendManagerService {

	static private Logger LOG = Logger
			.getLogger(BackendManagerServiceSML.class);
	// the bean object that stores the form elements
	private Properties activeBeanNamesRegex;
	private EditorContext editorContext;

	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}


	public Properties getActiveBeanNamesRegex() {
		return activeBeanNamesRegex;
	}

	public void setActiveBeanNamesRegex(Properties activeBeanNamesRegex) {
		this.activeBeanNamesRegex = activeBeanNamesRegex;
	}


	/**
	 * Merges the current bean values with the XML stored in mMergeDocument
	 *
	 * @return Document with merged values or null if on of the input is empty
	 */
	@Override
	public synchronized Document mergeBackend() {
		if (getBackend() != null && getBackend().getStorage() != null
				&& getMergeDocument() != null) {
			Document newDoc = getMergeDocument();

			for (Map.Entry<String, BaseBean> lEntry : getBackend().getStorage()
					.entrySet()) {
				// check if bean should be merged
				if (isBeanActive(lEntry.getKey())) {
					newDoc =getBeanTransformer().mergeToISO(lEntry.getValue(),
							newDoc);
				}
			}

			return newDoc;
		}
		return null;
	}

	public boolean isBeanActive(String beanName) {
		String resourceType = getResourceType();
		String regex = this.activeBeanNamesRegex.getProperty(resourceType);
		// iterate through map of beans
		if (!regex.equals("")) {
			if (beanName.matches(regex)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Simply determines the resource type from the current backend
	 *
	 * @return
	 */
	@Override
	public String getResourceType() {
		if (getMergeDocument() != null) {
			XPathUtil lUtil = new XPathUtil();
			EditorContext ec = editorContext;
			lUtil.setContext(ec);
			String resourceType = lUtil
					.evaluateAsString("//gmd:hierarchyLevel/*/@codeListValue",
							getMergeDocument());

			if (resourceType.equals("")) {
				resourceType = lUtil
						.evaluateAsString(
								"//sml:System/sml:classification/sml:ClassifierList/sml:classifier/sml:Term[@definition='urn:ogc:def:classifier:OGC:1.0:sensorType']/sml:value",
								getMergeDocument());
				if (resourceType.equals("")) {
					String bool = lUtil
							.evaluateAsString("boolean(//sml:identification)",
									getMergeDocument());
					if (bool.equals("true")) {
						resourceType = "sensor";
					}
				}
			}
			return resourceType;
		}
		return null;
	}
	/**
	 * Returns the current file identifier form the backend
	 *
	 * @return
	 */
	@Override
	public String getFileIdentifier() {
		String resource = getBackend().getResourceType();
		if (!resource.contains("sensor")) {
			Object o = getBackend().getStorage().get("fileIdentifier");
			if (o != null) {
				return ((FileIdentifierBean) o).getId();
			}
		} else {
			Object o = getBackend().getStorage().get("smlIdentifier");
			if (o != null) {
				return ((FileIdentifierBean) o).getId();

			}
		}
		return null;
	}

	/**
	 * Generates a new UUID as a metadata identifier
	 */
	@Override
	public void newMetadataIdentifier() {
		String newID = UUID.randomUUID().toString();

		Object o = getBackend().getStorage().get("fileIdentifier");
		if (o != null) {
			LOG.info("Updating metadata identifier fileIdentifier...");
			((FileIdentifierBean) o).setId(newID);
			if (LOG.isDebugEnabled()) {
				LOG.debug("New identifier is: " + newID);
			}
		}

		o = getBackend().getStorage().get("smlIdentifier");
		if (o != null) {
			LOG.info("Updating metadata identifier smlIdentifier...");
			((FileIdentifierBean) o).setId(newID);
			if (LOG.isDebugEnabled()) {
				LOG.debug("New smlIdentifier is: " + newID);
			}
		}

	}
}
