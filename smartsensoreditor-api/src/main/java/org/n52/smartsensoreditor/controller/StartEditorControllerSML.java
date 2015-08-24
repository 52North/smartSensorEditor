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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.apache.log4j.Logger;
import org.n52.smartsensoreditor.beans.StartEditorBeanSML;
import org.n52.smartsensoreditor.cswclient.facades.TransactionResponseSOS;
import org.n52.smartsensoreditor.dao.SOSCatalogService;
import org.n52.smartsensoreditor.dao.SOSWebServiceDescriptionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import de.conterra.smarteditor.clients.RequestFactory;
import de.conterra.smarteditor.controller.StartEditorController;
import de.conterra.smarteditor.cswclient.facades.TransactionResponse;
import de.conterra.smarteditor.dao.AbstractCatalogService;
import de.conterra.smarteditor.dao.WebServiceDescriptionDAO;
import de.conterra.smarteditor.dao.WebServiceDescriptionException;
import de.conterra.smarteditor.service.XSLTTransformerService;
import de.conterra.smarteditor.util.DOMUtil;

/**
 * Start controller for smartEditor
 *
 */

@Controller
public class StartEditorControllerSML extends StartEditorController {

	protected static final Logger LOG = Logger
			.getLogger(StartEditorControllerSML.class);

	@Resource(name = "requestFactory")
	private RequestFactory requestFactory;

	@Resource(name = "SOSCatalogServiceDAO")
	private SOSCatalogService catalogServiceSOS;

	@Resource(name="transformerFiles" )
	private Map<String, String> transformerFiles;

	@Resource(name = "xsltTransformerService")
	private XSLTTransformerService xsltTransformer;

	private static final String SOS_SERVICE_TYPE = "SOS";  //Test if in the GUI was chosen this value, see codelist_enumeration.xml "enumeration.servicetype[9]"
	private static final String SOS_Operation_DESCRIBE="DescribeSensor"; //Test if in the GUI was chosen this value, see codelist_enumeration.xml "enumeration.SOS_Operation[0]"
	private static final String SOS_Operation_DELETE="DeleteSensor"; //Test if in the GUI was chosen this value, see codelist_enumeration.xml "enumeration.SOS_Operation[1]"
	private String mfinishView = "editor.finished";

	public RequestFactory getRequestFactory() {
		return requestFactory;
	}


	public void setRequestFactory(RequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}



	public SOSCatalogService getSOSCatalogServiceDAO() {
		return catalogServiceSOS;
	}

	public void setSOSCatalogServiceDAO(SOSCatalogService catalogServiceSOS) {
		this.catalogServiceSOS = catalogServiceSOS;
	}

	public XSLTTransformerService getXsltTransformer() {
		return xsltTransformer;
	}

	public void setXsltTransformer(XSLTTransformerService xsltTransformer) {
		this.xsltTransformer = xsltTransformer;
	}


	public Map<String, String> getTransformerFiles() {
		return transformerFiles;
	}


	public void setTransformerFiles(Map<String, String> transformerFiles) {
		this.transformerFiles = transformerFiles;
	}

	

	public String getFinishView() {
		return mfinishView;
	}


	public void setFinishView(String finishView) {
		this.mfinishView = finishView;
	}


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
			ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceProcedureIDForSOS", "errors.service.procedureIDForSOS.empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "serviceOperationForSOS", "errors.service.operationForSOS.empty");
		}

		if (pResult.hasErrors()) {
			LOG.debug("Form validation errors: " + pResult);
			// return form view
			return new ModelAndView(getFormView(), getModelMap());
		}
		//Set service URL
		String serviceUrl=pEditorBean.getServiceUrl();
		//Create webservice
		if(pEditorBean.getServiceType().equalsIgnoreCase(SOS_SERVICE_TYPE)){//serviceTypes are defined in codelist_enumeration.xml in identifier: CT_ServiceTypeExt.
			LOG.debug("Put SOS values into webserviceDescriptionDAO");
			StartEditorBeanSML editorBeanSML = (StartEditorBeanSML) pEditorBean;
			//Set procedureID
			String procId = editorBeanSML.getServiceProcedureIDForSOS();
			LOG.debug("Procedure ID set to '" + procId + "'");
			//Set token
			String token = editorBeanSML.getServiceTokenForSOS();
			LOG.debug("Token is set to '" + token + "'");


			//For request
			Document catalogRequest = null;
			Document catalogResponse=null;
			Map parameterMap = new HashMap();
			parameterMap.put("procedureId", procId);

			SOSCatalogService sosService = getSOSCatalogServiceDAO();
			sosService.init(serviceUrl);
			sosService.addRequestHeader("Authorization", token);

			//When a sensor should be edited
			if (editorBeanSML.getServiceOperationForSOS().equalsIgnoreCase(SOS_Operation_DESCRIBE)) {
				//Create Request and do transaction
				catalogRequest = getRequestFactory().createRequest("get" , parameterMap);
				
				catalogResponse = sosService.transaction(catalogRequest);//does it really throw an exception??
				
				if(catalogResponse==null){
					Map<String, Object> lModel=getModelMap();
					lModel.put("response", new TransactionResponseSOS());
					lModel.put("serverError","errors.service.connect.request");
					lModel.put("sourcePage","startService");
					return new ModelAndView(getFinishView(), lModel);
				}
				//For Transformation
				Document sensorML = DOMUtil.newDocument(true);
				Source source = new DOMSource(catalogResponse);
				Result result = new DOMResult(sensorML);

				String transformerFilePath=getTransformerFiles().get(SOS_SERVICE_TYPE.toLowerCase());
				getXsltTransformer().setRulesetSystemID(transformerFilePath);
				// transform
				getXsltTransformer().transform(source, result);
				getBackendService().initBackend(sensorML);
				
				getBackendService().setUpdate(true);
				return new ModelAndView(getSuccessView());

			}
			//When a sensor should be deleted
			if (editorBeanSML.getServiceOperationForSOS().equalsIgnoreCase(SOS_Operation_DELETE)) {
				catalogRequest = getRequestFactory().createRequest("delete" , parameterMap);
				catalogResponse = sosService.transaction(catalogRequest);
				Map<String, Object> lModel = new HashMap<String, Object>();
				lModel.put("sourcePage","startService");
				if(catalogResponse==null){
					lModel.put("response", new TransactionResponseSOS());
					lModel.put("serverError","errors.service.connect.request");
					return new ModelAndView(getFinishView(), lModel);
				}
			
				lModel.put("response", new TransactionResponseSOS(catalogResponse));
				return new ModelAndView(getFinishView(), lModel);
			}
		}else{
			WebServiceDescriptionDAO dao =  getServiceFactory().getDescriptionDAO(pEditorBean.getServiceType().toLowerCase());
			dao.setUrl(serviceUrl);
			LOG.trace("Current dao: " + dao);
			if (!"".equals(pEditorBean.getServiceName())) {
				dao.setServiceName(pEditorBean.getServiceName());
			}
			try {
				Document lDoc = dao.getDescription();
				if (LOG.isTraceEnabled()) {
					String docString = DOMUtil.convertToString(lDoc, true);
					LOG.trace("Retrieved document from DAO: " + docString);
				}
				if (lDoc != null) {
					getBackendService().setUpdate(true);

					getBackendService().initBackend(lDoc);
					return new ModelAndView(getSuccessView());
				}
			} catch (WebServiceDescriptionException e) {
				pResult.rejectValue("serviceUrl", "errors.service.connect", new Object[]{e.getMessage()}, "Capabilities error");
				return new ModelAndView(getFormView(), getModelMap());
			}


		}
		return new ModelAndView(getFormView());

	}
}
