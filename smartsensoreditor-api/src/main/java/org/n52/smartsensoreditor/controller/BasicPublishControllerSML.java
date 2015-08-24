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

import de.conterra.smarteditor.beans.IConfigOptions;
import de.conterra.smarteditor.beans.PublishBean;
import de.conterra.smarteditor.beans.UserInfoBean;
import de.conterra.smarteditor.clients.RequestFactory;
import de.conterra.smarteditor.clients.SoapClient;
import de.conterra.smarteditor.common.workflow.GenericWorkflowManager;
import de.conterra.smarteditor.common.workflow.IState;
import de.conterra.smarteditor.controller.BasicPublishController;
import de.conterra.smarteditor.cswclient.facades.TransactionResponse;
import de.conterra.smarteditor.dao.AbstractCatalogService;
import de.conterra.smarteditor.dao.CatalogServiceDAO;
import de.conterra.smarteditor.dao.LockManager;
import de.conterra.smarteditor.service.BackendManagerService;

import org.apache.log4j.Logger;
import org.n52.smartsensoreditor.beans.PublishBeanSML;
import org.n52.smartsensoreditor.cswclient.facades.TransactionResponseSOS;
import org.n52.smartsensoreditor.dao.SOSCatalogService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * This controller publishes a metadata document using the plain CSW-T interface
 * without any addition features (user id, locking, etc...)
 */
public class BasicPublishControllerSML extends BasicPublishController {

	protected static final Logger LOG = Logger.getLogger(BasicPublishControllerSML.class);
	
	private GenericWorkflowManager operationSOSManager;
	
	
	public GenericWorkflowManager getOperationSOSManager() {
		return operationSOSManager;
	}


	public void setOperationSOSManager(GenericWorkflowManager operationSOSManager) {
		this.operationSOSManager = operationSOSManager;
	}


	/**
	 * This is triggered when the form is submitted
	 *
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {
		PublishBeanSML lBean = (PublishBeanSML) command;
		Document doc = getBackendManager().mergeBackend();
		Document catalogRequest = null;
		//get the State using the selected operation in selectStates.jsp
		IState selectedState=operationSOSManager.getState(lBean.getServiceOperationSOS(), request.getLocale());  
		// reset update
		getBackendManager().setUpdate(false);
		
//		Map<String, String[]> parameters
//		parameters.put("sosmetdata", new String[] {"b", "a"})
		
		// create request
		catalogRequest = getRequestFactory().createRequest(selectedState.getStateId() , doc); //, parameters);
		
		// add additional fields for SOS publish
		AbstractCatalogService service = getCatalogService();
		if (service instanceof SOSCatalogService) {
			SOSCatalogService sosService = (SOSCatalogService) service;
			sosService.init(lBean.getServiceUrlSOS());
			sosService.addRequestHeader("Authorization", lBean.getServiceTokenSOS());
		}
		
		Document catalogResponse = getCatalogService().transaction(catalogRequest);
		Map<String, Object> lModel = new HashMap<String, Object>();
		lModel.put("sourcePage","publish");
		if(catalogResponse==null){
			lModel.put("response", new TransactionResponseSOS());
			lModel.put("serverError","errors.service.connect.request");
			return new ModelAndView(getSuccessView(), lModel);
		}
		if(getBackendManager().getResourceType().equals("sensor")){
			lModel.put("response", new TransactionResponseSOS(catalogResponse));
		}else{
			lModel.put("response", new TransactionResponse(catalogResponse));
		}
		
		return new ModelAndView(getSuccessView(), lModel);
	}
}
