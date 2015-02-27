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
package de.conterra.smarteditor.controller;

import de.conterra.smarteditor.admin.TemplateManager;
import de.conterra.smarteditor.beans.UserInfoBean;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller handles the persistence of a template
 * 
 * @author kse Date: 17.03.2010 Time: 16:25:35
 */
public class SaveTemplateController implements Controller {

	private static Logger LOG = Logger.getLogger(SaveLocalController.class);

	private BackendManagerService mBackendService;
	private TemplateManager mTemplateManager;
	private UserInfoBean mUserInfo;

	public UserInfoBean getUserInfo() {
		return mUserInfo;
	}

	public void setUserInfo(UserInfoBean pUserInfo) {
		mUserInfo = pUserInfo;
	}

	public BackendManagerService getBackendService() {
		return mBackendService;
	}

	public void setBackendService(BackendManagerService pBackendService) {
		mBackendService = pBackendService;
	}

	public TemplateManager getTemplateManager() {
		return mTemplateManager;
	}

	public void setTemplateManager(TemplateManager templateManager) {
		this.mTemplateManager = templateManager;
	}

	/**
	 * @param request
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse pResponse) throws Exception {
		// get backend document
		Document lMerge = mBackendService.mergeBackend();
		XPathUtil lUtil = new XPathUtil();
		lUtil.setContext(new EditorContext());
		String lTitle = lUtil.evaluateAsString("//gmd:title/*/text()", lMerge);
		if (lTitle.equals("")) {
			lTitle= lUtil.evaluateAsString(
					"/*/gml:identifier/text()", lMerge);
		}
		try {
			mTemplateManager.saveTemplate(lTitle, "MD_Metadata", mUserInfo
					.getUserId() != null ? mUserInfo.getUserId() : "",
					mUserInfo.getGroupId() != null ? mUserInfo.getGroupId()
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
