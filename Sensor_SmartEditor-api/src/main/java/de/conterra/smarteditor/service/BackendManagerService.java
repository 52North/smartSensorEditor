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
package de.conterra.smarteditor.service;

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
public class BackendManagerService {

	static private Logger LOG = Logger.getLogger(BackendManagerService.class);
	// know validators
	private Map<String, SchematronValidator> validators;
	// the bean object that stores the form elements
	private BackendBean backend;
	// the XML Doc with with the values will be merged/updated
	private Document mergeDocument;

	// necessary transformers
	private XSLTTransformerService xsltTransformer;
	private BeanTransformerService beanTransformer;

	private boolean update;

	private LockManager lockManager;

	private Properties activeBeanNamesRegex;

	private Pattern activeBeanNamesPattern = null;

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public LockManager getLockManager() {
		return lockManager;
	}

	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	public BeanTransformerService getBeanTransformer() {
		return beanTransformer;
	}

	public void setBeanTransformer(BeanTransformerService pBeanTransformer) {
		beanTransformer = pBeanTransformer;
	}

	public XSLTTransformerService getXsltTransformer() {
		return xsltTransformer;
	}

	public void setXsltTransformer(XSLTTransformerService pXsltTransformer) {
		xsltTransformer = pXsltTransformer;
	}

	public BackendBean getBackend() {
		return backend;
	}

	public void setBackend(BackendBean pBackend) {
		backend = pBackend;
	}

	public Map<String, SchematronValidator> getValidators() {
		return validators;
	}

	public void setValidators(Map<String, SchematronValidator> pValidators) {
		validators = pValidators;
	}

	public Document getMergeDocument() {
		return mergeDocument;
	}

	public void setMergeDocument(Document pMergeDocument) {
		mergeDocument = pMergeDocument;
	}

	public Properties getActiveBeanNamesRegex() {
		return activeBeanNamesRegex;
	}

	public void setActiveBeanNamesRegex(Properties activeBeanNamesRegex) {
		this.activeBeanNamesRegex = activeBeanNamesRegex;
	}

	/**
	 * Initializes the backend beans with a given document
	 *
	 * @param pMetadata
	 *            XML Metadata document
	 * @param pXslt
	 *            optional Xslt to convert pMetadata to ISO 19139
	 */
	public synchronized void initBackend(Document pMetadata, String pXslt) {
		LOG.info("Initializing backend with document...");
		if (pXslt != null && !pXslt.equals("")) {
			// transform
			if (pXslt.startsWith("http")) {
				URL lXsltUrl = null;
				try {
					lXsltUrl = new URL(pXslt);
					xsltTransformer.setRuleset(DOMUtil.createDocumentFromURL(
							lXsltUrl, true));
				} catch (MalformedURLException e) {
					LOG.error("Could not read URL: " + pXslt);
					LOG.error(e.getMessage());
				}
			} else {
				// set the system id
				xsltTransformer.setRulesetSystemID(pXslt);
			}
			Document lDoc = DOMUtil.newDocument(true);
			Source lSource = new DOMSource(pMetadata);
			Result lResult = new DOMResult(lDoc);
			// transform
			xsltTransformer.transform(lSource, lResult);
			initBackend(lDoc);
		} else {
			initBackend(pMetadata);
		}
	}

	/**
	 * Initializes the backend from an external URL. Optional, an XSLT can be
	 * provided
	 *
	 * @param pMetadataURL
	 *            external URL to resource
	 * @param pXsltURL
	 *            XSLT URL (optional). If present, MD document will be
	 *            transformed.
	 */
	public synchronized void initBackend(URL pMetadataURL, URL pXsltURL)
			throws BackendManagerException {
		LOG.info("Initializing backend with external URL ...");
		if (LOG.isDebugEnabled()) {
			LOG.debug("URL: " + pMetadataURL.toExternalForm());
			LOG.debug("XSLT: " + pXsltURL);
		}
		Document lMd = DOMUtil.createDocumentFromURL(pMetadataURL, true);
		if (pXsltURL != null) {
			initBackend(lMd, pXsltURL.toExternalForm());
		} else {
			initBackend(lMd);
		}
	}

	/**
	 * Initializes the backend beans values based on pDocument
	 *
	 * @param pDocument
	 *            valid ISO 19139 Document
	 */
	public synchronized void initBackend(Document pDocument) {
		Map<String, BaseBean> lNewBackend = new ConcurrentHashMap<String, BaseBean>();
		setMergeDocument(pDocument);
		if (backend != null && backend.getStorage() != null) {
			// itreate through map
			for (Map.Entry<String, BaseBean> lEntry : backend.getStorage()
					.entrySet()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Initializing bean with id '" + lEntry.getKey()
							+ "'");
				}
				BaseBean lNewBean = beanTransformer.initBean(lEntry.getValue(),
						getMergeDocument());
				lNewBackend.put(lEntry.getKey(), lNewBean);
			}
		}
		if (backend == null) {
			backend = new BackendBean();
		}
		// set new beans
		backend.setStorage(lNewBackend);
		LOG.info("Backend successfully initialized");
		// set resource typ
		backend.setResourceType(getResourceType());
	}

	/**
	 * Initializes the backend beans based on the resource type Standard
	 * templates will be used for initializing the content
	 *
	 * @param pResourceType
	 *            one of MD_ScopeCode
	 * @throws BackendManagerService
	 */
	public synchronized void initBackend(String pResourceType)
			throws BackendManagerException {
		if (backend != null && backend.getStorage() != null) {
			// validates the input String and checks is the type is a service
			if (pResourceType != null && pResourceType.equals("service")) {
				setMergeDocument(DOMUtil.createDocumentFromSystemID(
						"/templates/service.xml", true));
			} else {
				StringBuffer buffer = new StringBuffer();
				buffer.append("/templates/");
				buffer.append(pResourceType);
				buffer.append(".xml");
				if (LOG.isDebugEnabled()) {
					LOG.debug("Attempting to load resource: "
							+ buffer.toString());
				}
				Resource lRes = new DefaultResourceLoader().getResource(buffer
						.toString());
				if (!lRes.isReadable()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Resource not readable");
						LOG.debug("Loading default 'dataset.xml' instead");
					}
					lRes = new DefaultResourceLoader()
							.getResource("templates/dataset.xml");
					setMergeDocument(DOMUtil.newDocument(true));
					Map<String, Object> lParameterMap = new ConcurrentHashMap<String, Object>();
					lParameterMap.put("resourceType", pResourceType);
					// add parameters to transformer
					xsltTransformer.setParameters(lParameterMap);
					// set ruleset for transformation
					xsltTransformer
							.setRulesetSystemID("/templates/applyResourceType.xslt");
					// set source document
					// transform
					try {
						Source lSource = new StreamSource(lRes.getInputStream());
						Result lResult = new DOMResult(getMergeDocument());
						xsltTransformer.transform(lSource, lResult);
					} catch (IOException e) {
						LOG.error(e.getMessage(), e);
						throw new BackendManagerException(e);
					}
				} else {
					try {
						setMergeDocument(DOMUtil.createFromStream(
								lRes.getInputStream(), true));
					} catch (IOException e) {
						LOG.error(e.getMessage());
						throw new BackendManagerException(e);
					}
				}
			}
			initBackend(getMergeDocument());
		}
	}

	/**
	 * Inits backend with service xml with a given type and identifier
	 * <p/>
	 * Required by federated catalouge calls.
	 *
	 * @param pServiceType
	 *            CSW or else
	 * @param pIdentifier
	 *            fileIdentifier that shall be used for the service instance
	 */
	public synchronized void initBackend(String pServiceType, String pIdentifier)
			throws BackendManagerException {
		if (backend != null && backend.getStorage() != null) {
			Document lResultDoc = DOMUtil.newDocument(true);
			Document lLocalDoc = DOMUtil.createDocumentFromSystemID(
					"/templates/service.xml", true);
			Map<String, Object> lParameterMap = new ConcurrentHashMap<String, Object>();
			lParameterMap.put("serviceType", pServiceType);
			lParameterMap.put("fileIdentifier", pIdentifier);
			// add parameters to transformer
			xsltTransformer.setParameters(lParameterMap);
			// set ruleset for transformation
			xsltTransformer
					.setRulesetSystemID("/templates/applyServiceType.xslt");
			// transform
			try {
				Source lSource = new DOMSource(lLocalDoc);
				Result lResult = new DOMResult(lResultDoc);
				xsltTransformer.transform(lSource, lResult);
			} catch (TransformerException e) {
				LOG.error(e.getMessage(), e);
				throw new BackendManagerException(e);
			}
			// check and create resource type in document
			initBackend(lResultDoc);
		}
	}

	/**
	 * Merges the current bean values with the XML stored in mMergeDocument
	 *
	 * @return Document with merged values or null if on of the input is empty
	 */
	public synchronized Document mergeBackend() {
		if (backend != null && backend.getStorage() != null
				&& getMergeDocument() != null) {
			Document newDoc = getMergeDocument();

			for (Map.Entry<String, BaseBean> lEntry : backend.getStorage()
					.entrySet()) {
				// check if bean should be merged
				if (isBeanActive(lEntry.getKey())) {
					newDoc = beanTransformer.mergeToISO(lEntry.getValue(),
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
	 * Validates the current backend content with the given validator id
	 *
	 * @param pValidatorId
	 *            id of a validator stores in validators
	 * @return Schematron validation report.
	 */
	public Document validate(String pValidatorId)
			throws BackendManagerException {
		if (validators != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Validating metadata document against ruleset with id "
						+ pValidatorId);
			}
			SchematronValidator lCurrent = validators.get(pValidatorId);
			if (lCurrent != null) {
				try {
					Document lReport = DOMUtil.newDocument(true);
					Source lSource = new DOMSource(mergeBackend());
					LOG.debug("mergedBackend for validation: "
							+ DOMUtil.convertToString(mergeBackend(), true));
					Result lResult = new DOMResult(lReport);
					// set ruleset Id AFTER merging the document
					xsltTransformer.setRulesetSystemID(lCurrent
							.getRulesetSystemID());
					// now transform
					xsltTransformer.transform(lSource, lResult);
					return lReport;
				} catch (TransformerException e) {
					LOG.error(e.getMessage(), e);
					throw new BackendManagerException(e);
				}
			}
		}
		return null;
	}

	/**
	 * Simply determines the resource type from the current backend
	 *
	 * @return
	 */
	public String getResourceType() {
		if (getMergeDocument() != null) {
			XPathUtil lUtil = new XPathUtil();
			EditorContext ec = new EditorContext();
			lUtil.setContext(ec);
			String resourceType = lUtil
					.evaluateAsString("//gmd:hierarchyLevel/*/@codeListValue",
							getMergeDocument());
			if (resourceType == "") {
				String bool = lUtil.evaluateAsString("boolean(//sml:System)",
						getMergeDocument());
				if (bool.equals("true")) {
					resourceType = "sensor";
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
	public String getFileIdentifier() {
		String resource = backend.getResourceType();
		if (!resource.equals("sensor")) {
			Object o = getBackend().getStorage().get("fileIdentifier");
			if (o != null) {
				return ((FileIdentifierBean) o).getId();
			}
		} else {
			Object o = getBackend().getStorage().get("smlUniqueID");
			if (o != null) {
				return ((FileIdentifierBean) o).getId();

			}
		}
		return null;
	}

	/**
	 * Generates a new UUID as a metadata identifier
	 */
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
		
			 o = getBackend().getStorage().get("smlUniqueID");
			if (o != null) {
				LOG.info("Updating metadata identifier smlUniqueID...");
				((FileIdentifierBean) o).setId(newID);
				if (LOG.isDebugEnabled()) {
					LOG.debug("New identifier is: " + newID);
				}
			}
		

	}
}
