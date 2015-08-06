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

import org.apache.log4j.Logger;
import org.n52.smartsensoreditor.beans.StartEditorBeanSML;
import org.n52.smartsensoreditor.dao.SOSWebServiceDescriptionDAO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import com.sun.xml.bind.Util;

import de.conterra.smarteditor.beans.StartEditorBean;
import de.conterra.smarteditor.controller.StartEditorController;
import de.conterra.smarteditor.dao.WebServiceDescriptionDAO;
import de.conterra.smarteditor.dao.WebServiceDescriptionException;
import de.conterra.smarteditor.util.DOMUtil;

/**
 * Start controller for smartEditor
 *
 * @author kse Date: 26.03.2010 Time: 12:46:34
 */

@Controller
public class StartEditorControllerSML extends StartEditorController {

	protected static final Logger LOG = Logger
			.getLogger(StartEditorControllerSML.class);

	private static final String SOS_SERVICE_TYPE = "SOS";  //Test if in the GUI was chosen this value, see codelist_enumeration.xml "enumeration.servicetype[9]"
    private static final String SOS_Operation_DELETE="DeleteSensor"; //Test if in the GUI was chosen this value, see codelist_enumeration.xml "enumeration.SOS_Operation[0]"

    /**
	 * Starts editor with a service description
	 *
	 * @param pEditorBean
	 * @param pResult
	 * @return
	 */
    @RequestMapping(value = "/startServiceSOS", method = RequestMethod.POST)
    public ModelAndView startServiceHandler(@ModelAttribute("startEditorBeanSML") StartEditorBeanSML pEditorBean,
                                            BindingResult pResult) {
    	//Error handling
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceUrl", "errors.service.url.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceType", "errors.service.type.empty");
        
        if (pEditorBean.getServiceType().equalsIgnoreCase("ARCIMS")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceName", "errors.service.name.empty");
        }
        
        if (pEditorBean.getServiceType().equalsIgnoreCase(SOS_SERVICE_TYPE)) {
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceTokenForSOS", "errors.service.tokenForSOS.empty");
            if(pEditorBean.getServiceOperationForSOS().equalsIgnoreCase(SOS_Operation_DELETE)){
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceProcedureIDForSOS", "errors.service.procedureIDForSOS.empty");
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceOperationForSOS", "errors.service.operationForSOS.empty");
        }
        
        if (pResult.hasErrors()) {
        	LOG.debug("Form validation errors: " + pResult);
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        
        //Create webservice
        WebServiceDescriptionDAO dao =  getServiceFactory().getDescriptionDAO(pEditorBean.getServiceType().toLowerCase());
        dao.setUrl(pEditorBean.getServiceUrl());
        LOG.trace("Current dao: " + dao);
        if (!"".equals(pEditorBean.getServiceName())) {
			dao.setServiceName(pEditorBean.getServiceName());
		}
           //Only for SOS
        if(pEditorBean.getServiceType().equalsIgnoreCase(SOS_SERVICE_TYPE))  {
        	LOG.debug("Put SOS values into webserviceDescriptionDAO");
        		StartEditorBeanSML editorBeanSML = (StartEditorBeanSML) pEditorBean;
    		
    			if (dao instanceof SOSWebServiceDescriptionDAO) {
    				SOSWebServiceDescriptionDAO sosDao = (SOSWebServiceDescriptionDAO) dao;
    				//Set procedureID
    				String procId = editorBeanSML.getServiceProcedureIDForSOS();
    				sosDao.setServiceProcedureIDForSOS(procId);
					LOG.debug("Procedure ID set to '" + procId + "'");
					//Set token
					String token = editorBeanSML.getServiceTokenForSOS();
    				sosDao.setServiceTokenForSOS(token);
					LOG.debug("Procedure ID set to '" + token + "'");
					
					
					//When a sensor should be deleted
					 if (editorBeanSML.getServiceOperationForSOS().equalsIgnoreCase(SOS_Operation_DELETE)) {
				        	LOG.debug("sensor should be deleted");
				            return new ModelAndView(getFormView(), getModelMap());
				        }
    			}
    			else  {
            		throw new RuntimeException("editor bean service type is " + SOS_SERVICE_TYPE + " but DAO instance is of type " + dao.getClass().getName() + ", should be " + SOSWebServiceDescriptionDAO.class.getName());
            	}
        }
       
		try {
			Document lDoc = dao.getDescription();
			if (LOG.isTraceEnabled()) {
				String docString = DOMUtil.convertToString(lDoc, true);
				LOG.trace("Retrieved document from DAO: " + docString);
			}
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
