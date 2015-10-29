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
package org.n52.smartsensoreditor.controller;

import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller to save a metadata document on the local hard disk
 * <p/>
 *
 */
public class SaveTemplateController extends de.conterra.smarteditor.controller.SaveTemplateController {

	private static Logger LOG = Logger.getLogger(SaveTemplateController.class);

	private EditorContext editorContext;
        
        private List<String> titleXpath;

        public void setTitleXpath(List<String> titleXpath) {
            this.titleXpath = titleXpath;
        }

	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}

        @Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse pResponse) throws Exception {
		// get backend document
		Document lMerge = getBackendService().mergeBackend();
		XPathUtil lUtil = new XPathUtil();
		lUtil.setContext(editorContext);
                
                String lTitle = "title";
                for (String xpath : titleXpath) {
                    String evalFileId = lUtil.evaluateAsString(xpath, lMerge).trim();
                    if (evalFileId != null) {
                        lTitle = evalFileId;
                        if(LOG.isDebugEnabled()) {
                            LOG.debug(String.format("Derived title %s using XPath %s", evalFileId, xpath));
                        }
                        break;
                    }
                }
                
		try {
			getTemplateManager().saveTemplate(lTitle, "MD_Metadata", getUserInfo()
					.getUserId() != null ? getUserInfo().getUserId() : "",
							getUserInfo().getGroupId() != null ? getUserInfo().getGroupId()
							: "", "public", DOMUtil.convertToString(lMerge,
							false));
		} catch (Exception e) {
			LOG.error(e);
			pResponse.sendError(500, e.getMessage());
		}
		LOG.info("Template saved...");
		return null;
	}
}
