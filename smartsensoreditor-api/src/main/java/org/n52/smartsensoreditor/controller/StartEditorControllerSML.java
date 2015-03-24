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
 * @author kse
 *         Date: 26.03.2010
 *         Time: 12:46:34
 */

@Controller
public class StartEditorControllerSML extends StartEditorController{

    protected static final Logger LOG = Logger.getLogger(StartEditorControllerSML.class);
	TemplateManager mTemplateManager;
	
    /**
     * return the model for the form view
     *
     * @param pLocale
     * @return
     */
    private Map<String, Object> getModelMap(Locale pLocale) {
        // init stylesheets
        if (getModelMap() == null) {
        	setModelMap(new HashMap<String, Object>());
        }
        // init stylesheets
        if (getModelMap().get("xslt_import") == null) {
        	getModelMap().put("xslt_import", getFileSystem().getFiles(getConfig().getStylesheetImportDir()));
        }

        List<String> lCodeListIds = getConfig().getCodeListIds();
        for (String lCodeListId : lCodeListIds) {
            CodeListBean lBean = getFileSystem().getCodeList(pLocale, lCodeListId);
            if (lBean != null) {
            	getModelMap().put(lCodeListId, lBean);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Codelist with ID '" + lCodeListId + "' has been added");
                }
            } else {
                LOG.warn("Could not load codelist with ID '" + lCodeListId + "'");
            }
        }
        // init templates
        try {
            List<String> lTemplates = mTemplateManager.getExistingTemplateNames("MD_Metadata");
            if (lTemplates != null) {
                Collections.sort(lTemplates, String.CASE_INSENSITIVE_ORDER);
            }
            getModelMap().put("template_names", lTemplates);
            LOG.info("Templates initialized...");
        } catch (Exception e) {
            LOG.error("Error initializing/reading templates: " + e.getMessage());
        }

        if (getConfig().isDraftSupported()) {
            try {
                // init drafts
                getModelMap().put("draft_names", getCatalogService().getDraftNames());
                LOG.info("Drafts initialized...");
            } catch (RemoteException e) {
                LOG.error("Unable to get draft names from catalog: " + e.getMessage(), e);
            } catch (InvokerException e) {
                LOG.error("Unable to invoke catalog service: " + e.getMessage(), e);
            } catch (Exception e) {
                LOG.error("Unable to invoke catalog admin: " + e.getMessage(), e);
            }
        }
        getModelMap().put("startEditorBean", new StartEditorBean());
        return getModelMap();
    }

    /**
     * Set up any required reference data:
     * <p/>
     * <ul>
     * <li>templates</li>
     * <li>drafts</li>
     * <li>code lists</li>
     * <li>XSLT</li>
     * </ul>
     *
     * @param pRequest
     * @param pModel
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/startEditor")
    protected ModelAndView setupForm(HttpServletRequest pRequest,
                                     HttpServletResponse pResponse,
                                     ModelMap pModel) throws Exception {
        Cookie[] lCookies = pRequest.getCookies();
        if (lCookies != null) {
            for (Cookie cookie : pRequest.getCookies()) {
                // remove any smart editor related cookie
                if (cookie.getName() != null && cookie.getName().startsWith("tc_smartEditor")) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    pResponse.addCookie(cookie);
                }
            }
        }
        // add model data
        pModel.addAllAttributes(getModelMap(pRequest.getLocale()));
        // reset user Info update status
        getUserInfo().setUpdate(false);
        // return form view
        return new ModelAndView(getFormView());
    }


    /**
     * Start editor with file upload
     *
     * @param pEditorBean
     * @param pResult
     * @return
     */
    @RequestMapping(value = "/uploadDocument", method = RequestMethod.POST)
    public ModelAndView uploadDocumentHandler(@ModelAttribute("startEditorBean") StartEditorBean pEditorBean,
                                              BindingResult pResult) {
        LOG.info("Initializing with document upload");
        if (pEditorBean.getDocument() == null) {
            pResult.rejectValue("document", "errors.file.empty");
        }
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        // init backend
        getBackendService().initBackend(pEditorBean.getDocument(), pEditorBean.getXslt());
        // generate metadata identifier
        getBackendService().newMetadataIdentifier();
        // return model
        return new ModelAndView(getSuccessView());
    }

    /**
     * Start editor with new document
     *
     * @param pEditorBean
     * @param pResult
     * @return
     */
    @RequestMapping(value = "/startNew", method = RequestMethod.POST)
    public ModelAndView startNewHandler(@ModelAttribute("startEditorBean") StartEditorBean pEditorBean,
                                        BindingResult pResult) {
        LOG.info("Initializing with new document");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "resourceType", "errors.resourcetype.empty");
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        getBackendService().initBackend(pEditorBean.getResourceType());
        return new ModelAndView(getSuccessView());
    }

    /**
     * Start editor with existing draft
     *
     * @param pEditorBean
     * @param pResult
     * @return
     */
    @RequestMapping(value = "/startDraft", method = RequestMethod.POST)
    public ModelAndView startDraftHandler(@ModelAttribute("startEditorBean") StartEditorBean pEditorBean,
                                          BindingResult pResult) {
        LOG.info("Initializing from draft");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "fileIdentifier", "errors.draft.name.empty");
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        // get draft
        Document lDraft = null;
        try {
            lDraft = getCatalogService().getDraft(pEditorBean.getFileIdentifier());
            // init backend
            getBackendService().initBackend(lDraft);
        } catch (RemoteException e) {
            LOG.error(e);
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        // this is an update
        getUserInfo().setUpdate(true);
        // return view
        return new ModelAndView(getSuccessView());
    }

    /**
     * Deletes a draft with the given id
     *
     * @param pEditorBean
     * @param pResult
     * @return
     */
    @RequestMapping(value = "/deleteDraft", method = RequestMethod.POST)
    public ModelAndView deleteDraftHandler(@ModelAttribute("startEditorBean") StartEditorBean pEditorBean,
                                           BindingResult pResult,
                                           HttpServletRequest pRequest,
                                           HttpServletResponse pResponse) {
        LOG.info("Deleting draft");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "fileIdentifier", "errors.draft.name.empty");
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        try {
            try {
                getCatalogService().deleteDraft(pEditorBean.getFileIdentifier());
            } catch (RemoteException e) {
                LOG.error(e);
                pResponse.sendError(500, e.getMessage());
            } catch (Exception e) {
                LOG.error(e);
                pResponse.sendError(500, e.getMessage());
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        return new ModelAndView(getFormView(), getModelMap(pRequest.getLocale()));
    }


    /**
     * Start editor with existing template
     *
     * @param pEditorBean
     * @param pResult
     * @return
     */
    @RequestMapping(value = "/startTemplate", method = RequestMethod.POST)
    public ModelAndView startTemplateHandler(@ModelAttribute("startEditorBean") StartEditorBean pEditorBean,
                                             BindingResult pResult) {
        LOG.info("Initializing from template");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "templateName", "errors.template.name.empty");
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }


        String lTemplate = mTemplateManager.getTemplate(pEditorBean.getTemplateName(), "MD_Metadata");
        if (lTemplate != null) {
            getBackendService().initBackend(DOMUtil.createFromString(lTemplate, true));
            // generate new identifier for uniqueness
            getBackendService().newMetadataIdentifier();
        } else {
            LOG.warn("Could not load template with name " + pEditorBean.getTemplateName());
            return new ModelAndView(getFormView(), getModelMap());
        }

        return new ModelAndView(getSuccessView());
    }

    /**
     * Deletes a template with the given name
     *
     * @param pEditorBean
     * @param pResult
     * @return
     */
    @RequestMapping(value = "/deleteTemplate", method = RequestMethod.POST)
    public ModelAndView deleteTemplateHandler(@ModelAttribute("startEditorBean") StartEditorBean pEditorBean,
                                              BindingResult pResult,
                                              HttpServletRequest pRequest,
                                              HttpServletResponse pResponse) {
        LOG.info("Deleting template");
        ValidationUtils.rejectIfEmptyOrWhitespace(pResult, "templateName", "errors.template.name.empty");
        if (pResult.hasErrors()) {
            // return form view
            return new ModelAndView(getFormView(), getModelMap());
        }
        try {
            try {
                mTemplateManager.deleteTemplate(pEditorBean.getTemplateName(), "MD_Metadata");
            } catch (Exception e) {
                LOG.error(e);
                pResponse.sendError(500, e.getMessage());
            }
        } catch (IOException e) {
            LOG.error(e);
        }
        return new ModelAndView(getFormView(), getModelMap(pRequest.getLocale()));
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
        if(dao.getClass().toString().contains("SOS")){
        	((SOSWebServiceDescriptionDAO) dao).setServiceProcedureIDForSOS(pEditorBean.getServiceProcedureIDForSOS());	
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

    /**
     * Inits binder objects for form object.
     *
     * @param binder
     * @throws javax.servlet.ServletException
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder)
            throws ServletException {
        // to actually be able to convert Multipart instance to a String
        // we have to register a custom editor
        binder.registerCustomEditor(Document.class, new DocumentMultipartFileEditor());
    }
}
