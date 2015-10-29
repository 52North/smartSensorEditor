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

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.conterra.smarteditor.beans.BaseBean;
import de.conterra.smarteditor.beans.FileIdentifierBean;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;
import java.util.List;

/**
 * Service manages any access to and modification of the backend storage
 * <p/>
 *
 */
public class BackendManagerService extends de.conterra.smarteditor.service.BackendManagerService {

	static private Logger LOG = Logger
			.getLogger(BackendManagerService.class);
	
        // the bean object that stores the form elements
	private Properties activeBeanNamesRegex;
        
	private EditorContext editorContext;

        private List<String> resourceTypeXPaths;
        
        private String identifierStorageName;

        public void setIdentifierStorageName(String identifierStorageName) {
            this.identifierStorageName = identifierStorageName;
        }

        public void setResourceTypeXPaths(List<String> resourceTypeXPaths) {
            this.resourceTypeXPaths = resourceTypeXPaths;
        }
        
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
        String resourceType = null;
        if (getMergeDocument() != null) {
            XPathUtil lUtil = new XPathUtil();
            EditorContext ec = editorContext;
            lUtil.setContext(ec);
            for (String xpath : resourceTypeXPaths) {
                String rt =  lUtil.evaluateAsString(xpath, getMergeDocument());
                if(rt != null) {
                    resourceType = rt;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(String.format("Found resource type %s with XPath %s", resourceType, xpath));
                    }
                    break;
                }
            }
        }
        return resourceType;
    }

    /**
     * Returns the current file identifier form the backend
     *
     * @return
     */
    @Override
    public String getFileIdentifier() {
        // TODO should be renamed to getIdentifier()
        Object o = getBackend().getStorage().get(this.identifierStorageName);
        if (o != null) {
            return ((FileIdentifierBean) o).getId();
        }
        return null;
    }

    /**
     * Generates a new UUID as a metadata identifier
     */
    @Override
    public void newMetadataIdentifier() {
        Object o = getBackend().getStorage().get(this.identifierStorageName);
        if (o != null) {
            LOG.info("Updating metadata identifier...");
            String newID = UUID.randomUUID().toString();
            ((FileIdentifierBean) o).setId(newID);
            if (LOG.isDebugEnabled()) {
                LOG.debug("New identifier is: " + newID);
            }
        }
    }
}
