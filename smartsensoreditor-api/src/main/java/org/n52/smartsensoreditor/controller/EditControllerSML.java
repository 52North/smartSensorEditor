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

import com.jenkov.prizetags.tree.itf.ITree;

import de.conterra.smarteditor.beans.BackendBean;
import de.conterra.smarteditor.beans.CodeListBean;
import de.conterra.smarteditor.beans.IConfigOptions;
import de.conterra.smarteditor.beans.IMapOptions;
import de.conterra.smarteditor.beans.UserInfoBean;
import de.conterra.smarteditor.common.workflow.IState;
import de.conterra.smarteditor.common.workflow.IWorkflowManager;
import de.conterra.smarteditor.controller.EditController;
import de.conterra.smarteditor.dao.FileSystemDAO;
import de.conterra.smarteditor.dao.ThematicTreeDAO;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.support.LocalePropertyEditorRegistrar;

import org.apache.log4j.Logger;
import org.n52.smartsensoreditor.beans.BackendBeanSML;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Backend controller for the edit form.
 * <p/>
 *
 *
 */
public class EditControllerSML extends EditController {

	protected static final Logger LOG = Logger
			.getLogger(EditControllerSML.class);


	private IWorkflowManager operationSOSManager;

	public EditControllerSML() {
	}

	public IWorkflowManager getOperationSOSManager() {
		return operationSOSManager;
	}

	public void setOperationSOSManager(IWorkflowManager operationSOSManager) {
		this.operationSOSManager = operationSOSManager;
	}
/**
 * This method creates the model map for the selectStates.jsp. 
 * The values which can be selected in the boxes are created here.
 */
	@Override
	protected Map<String, Object> createModelMap(HttpServletRequest request) {
		String status = getWorkflowManager().getStatus(
				getBackendService().getFileIdentifier());
		String statusOperationSOS = "";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Document's file identifier is "
					+ getBackendService().getFileIdentifier());
			LOG.debug("Document's current status is " + status);
		}
		// default state
		if (status == null) {
			IState defaultState = (IState) getWorkflowManager()
					.getDefaultState();
			status = defaultState.getStateId();
			// SOS operation
			IState defaultOperationSOS = (IState) getOperationSOSManager()
					.getDefaultState();
			statusOperationSOS = defaultOperationSOS.getStateId();
			//if the metadata set has the status update, then set this as default 
			if(getBackendService().isUpdate()){
			defaultOperationSOS=(IState) getOperationSOSManager().getState("update", request.getLocale());
			statusOperationSOS = defaultOperationSOS.getStateId();
			}
		}
		List<IState> stateList = getWorkflowManager().getStates(
				getUserInfo().getTicket(), status, request.getLocale(), true);
		Map<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>();
		concurrentHashMap.put("transitionStates", stateList);
		IState lState = (IState) getWorkflowManager().getState(status,
				request.getLocale());
		concurrentHashMap.put("currentStateName", lState.getStateName());
		concurrentHashMap.put("currentStateId", lState.getStateId());

		// SOS OperationList
		List<IState> stateListOperationSOS = getOperationSOSManager().getStates(
				getUserInfo().getTicket(), statusOperationSOS, request.getLocale(), true);
		concurrentHashMap.put("operationsSOS", stateListOperationSOS);
		IState lOperationsSOS = (IState) getOperationSOSManager().getState(
				statusOperationSOS, request.getLocale());
		concurrentHashMap.put("currentOperationSOSName", lOperationsSOS.getStateName());
		concurrentHashMap.put("currentOperationSOSId", lOperationsSOS.getStateId());
        
		//Set the predefined values 
		concurrentHashMap.put("procedureIdSOS", getBackendService().getFileIdentifier()); //procedureId==smlIdentifier==<gml:identifier(unique identifier)
		//Set the token and the serviceUrl within the concurrentHashMap to insert them in the selectStates.jsp file.
		BackendBean backendBean=getBackendService().getBackend();
		
		if(backendBean instanceof BackendBeanSML){
			BackendBeanSML backendBeanSML=((BackendBeanSML)backendBean);
			
			String serviceUrl=backendBeanSML.getServiceURL();
			LOG.debug("Predefined serviceUrl is set to '" + serviceUrl + "'");
			if(serviceUrl!=null){
				concurrentHashMap.put("serviceURLSOS",serviceUrl);
			}
	
			String serviceTokenSOS=backendBeanSML.getServiceTokenSOS();
			LOG.debug("Predefines token is set to '" + serviceTokenSOS + "'");
			if(serviceTokenSOS!=null){
				concurrentHashMap.put("serviceTokenSOS",serviceTokenSOS);
			}
			
		}
		
		return concurrentHashMap;
	}

}
