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

import de.conterra.smarteditor.admin.TemplateManager;
import de.conterra.smarteditor.beans.CodeListBean;
import de.conterra.smarteditor.beans.IConfigOptions;
import de.conterra.smarteditor.beans.StartEditorBean;
import de.conterra.smarteditor.beans.UserInfoBean;
import de.conterra.smarteditor.controller.StartEditorController;
import de.conterra.smarteditor.cswclient.exception.InvokerException;
import de.conterra.smarteditor.dao.*;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.service.ValidatorService;
import de.conterra.smarteditor.support.DocumentMultipartFileEditor;
import de.conterra.smarteditor.util.DOMUtil;

import org.apache.log4j.Logger;
import org.n52.smartsensoreditor.beans.StartEditorBeanSML;
import org.n52.smartsensoreditor.dao.SOSWebServiceDescriptionDAO;
import org.opensaml.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Start controller for smartEditor
 *
 * @author kse Date: 26.03.2010 Time: 12:46:34
 */

@Controller
public class StartEditorControllerSML extends StartEditorController {

	protected static final Logger LOG = Logger
			.getLogger(StartEditorControllerSML.class);

	private static final String SOS_SERVICE_TYPE = "SOS";

	/**
	 * Starts editor with a service description
	 *
	 * @param pEditorBean
	 * @param pResult
	 * @return
	 */
	@Override
    @RequestMapping(value = "/startService", method = RequestMethod.POST)
    public ModelAndView startServiceHandler(@ModelAttribute("startEditorBeanSML") StartEditorBean pEditorBean,
                                            BindingResult pResult) {

        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceUrl", "errors.service.url.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceType", "errors.service.type.empty");
        if (pEditorBean.getServiceType().equalsIgnoreCase("ARCIMS")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceName", "errors.service.name.empty");
        }
        
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        
        WebServiceDescriptionDAO dao =  getServiceFactory().getDescriptionDAO(pEditorBean.getServiceType().toLowerCase());
        dao.setUrl(pEditorBean.getServiceUrl());
        
        // check if SOS-specific handling is needed
        if (pEditorBean.getServiceType().equalsIgnoreCase(SOS_SERVICE_TYPE)) {
        	// validate 
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceTokenForSOS", "errors.service.tokenForSOS.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceProcedureIDForSOS", "errors.service.procedureIDForSOS.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceOperationForSOS", "errors.service.operationForSOS.empty");
        	
        	LOG.debug("Starting SOS interaction");
        	// set SOS specific properties
        	if (pEditorBean instanceof StartEditorBeanSML) {
        		StartEditorBeanSML editoBeanSML = (StartEditorBeanSML) pEditorBean;
    		
    			if (dao instanceof SOSWebServiceDescriptionDAO) {
    				SOSWebServiceDescriptionDAO sosDao = (SOSWebServiceDescriptionDAO) dao;
    				String procId = editoBeanSML.getServiceProcedureIDForSOS();
    				sosDao.setServiceProcedureIDForSOS(procId);
					LOG.debug("Procedure ID set to '" + procId + "'");
    			}
    			else  {
            		throw new RuntimeException("editor bean service type is " + SOS_SERVICE_TYPE + " but DAO instance is of type " + dao.getClass().getName() + ", should be " + SOSWebServiceDescriptionDAO.class.getName());
            	}
    		}
        	else  {
        		throw new RuntimeException("editor bean service type is " + SOS_SERVICE_TYPE + " but editor bean instance is of type " + pEditorBean.getClass().getName());
        	}
        }
        
        if (!"".equals(pEditorBean.getServiceName())) {
            dao.setServiceName(pEditorBean.getServiceName());
        }
        try {
            Document lDoc = dao.getDescription();
            if (lDoc != null) {
                getBackendService().initBackend(lDoc);
                return new ModelAndView(getSuccessView());
            }
        } catch (WebServiceDescriptionException e) {
            pResult.rejectValue("serviceUrl", "errors.service.connect", new Object[]{e.getMessage()}, "Capabilities error");
            return new ModelAndView(getFormView(), getModelMap());
        }
        return new ModelAndView(getFormView());
    }
}
