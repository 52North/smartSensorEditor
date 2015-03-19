/**
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * con terra GmbH licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.n52.smarteditor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.conterra.smarteditor.xml.EditorContext;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.w3c.dom.Document;

import de.conterra.smarteditor.admin.TemplateManager;
import de.conterra.smarteditor.beans.UserInfoBean;
import de.conterra.smarteditor.controller.SaveLocalController;
import de.conterra.smarteditor.controller.SaveTemplateController;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.util.XPathUtil;

/**
 * Controller handles the persistence of a template
 * 
 * @author kse Date: 17.03.2010 Time: 16:25:35
 */
public class SaveTemplateControllerSML extends SaveTemplateController implements Controller {

	private static Logger LOG = Logger.getLogger(SaveLocalController.class);

	private BackendManagerService mBackendService;
	private TemplateManager mTemplateManager;
	private UserInfoBean mUserInfo;
	public BackendManagerService getmBackendService() {
		return mBackendService;
	}

	public void setmBackendService(BackendManagerService mBackendService) {
		this.mBackendService = mBackendService;
	}

	public TemplateManager getmTemplateManager() {
		return mTemplateManager;
	}

	public void setmTemplateManager(TemplateManager mTemplateManager) {
		this.mTemplateManager = mTemplateManager;
	}

	public UserInfoBean getmUserInfo() {
		return mUserInfo;
	}

	public void setmUserInfo(UserInfoBean mUserInfo) {
		this.mUserInfo = mUserInfo;
	}
	private EditorContext editorContext;



	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}
	

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
	    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse pResponse) throws Exception {
	        // get backend document
	        Document lMerge = mBackendService.mergeBackend();
	        XPathUtil lUtil = new XPathUtil();
	        lUtil.setContext(new EditorContext());
	        String lTitle = lUtil.evaluateAsString("//gmd:title/*/text()", lMerge);
	        try {
	            mTemplateManager.saveTemplate(lTitle, "MD_Metadata",
	                    mUserInfo.getUserId() != null ? mUserInfo.getUserId() : "",
	                    mUserInfo.getGroupId() != null ? mUserInfo.getGroupId() : "",
	                    "public",
	                    DOMUtil.convertToString(lMerge, false));
	        } catch (Exception e) {
	            LOG.error(e);
	            pResponse.sendError(500, e.getMessage());
	        }
	        LOG.info("Template saved...");
	        return null;
	    }
}
