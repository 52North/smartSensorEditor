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

import de.conterra.smarteditor.controller.SaveLocalController;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.w3c.dom.Document;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.Map;

/**
 * Controller to save a metadata document on the local hard disk
 * <p/>
 *
 */
public class SaveLocalControllerSML extends SaveLocalController  {

    private static Logger LOG = Logger.getLogger(SaveLocalControllerSML.class);
	private EditorContext editorContext;

	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}
    /**
     * Hanlde user request
     *
     * @param request
     * @param pResponse
     * @return
     * @throws Exception
     */
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse pResponse) throws Exception {
        // get document stored in backenservice
        Document lDoc = mBackendService.mergeBackend();
        XPathUtil lUtil = new XPathUtil();
        lUtil.setContext(editorContext);
        String lFileId = lUtil.evaluateAsString("//gmd:fileIdentifier/gco:CharacterString/text()", lDoc);
        if(lFileId.equals("")){
        	lFileId=lUtil.evaluateAsString("/*/gml:identifier/text()", lDoc);
        }
        lFileId = lFileId.trim();
        // set response attributes
        //pResponse.setContentType("application/x-download");
        pResponse.setContentType(getContentType() == null ? "application/x-download" : getContentType());
        // set header
        if (getSetHeader() != null) {
            for (Map.Entry<String, String> entry : getSetHeader().entrySet()) {
                pResponse.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // add header
        if (getAddHeader() != null) {
            for (Map.Entry<String, String> entry : getAddHeader().entrySet()) {
                pResponse.addHeader(entry.getKey(), entry.getValue());
            }
        }
        pResponse.setHeader("Content-disposition", "attachment; filename=" + lFileId + ".xml");
        if (LOG.isDebugEnabled())
            LOG.debug("Preparing download for document with id: " + lFileId);
        ServletOutputStream lOutputStream = pResponse.getOutputStream();
        try {
            // write document to servlet response
            TransformerFactory tf = TransformerFactory.newInstance();
            // identity
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(lDoc), new StreamResult(lOutputStream));
        } catch (Exception e) {
            pResponse.setHeader("Content-disposition", "attachment; filename=ExportError.xml");
            lOutputStream.println("<?xml version='1.0' encoding=\"UTF-8\" standalone=\"no\" ?>");
            lOutputStream.println("<ExportExceptionReport version=\"1.1.0\">");
            lOutputStream.println("    Request rejected due to errors.");
            lOutputStream.println("	   Reason: " + e.getMessage());
            lOutputStream.println("</ExportExceptionReport>");
            lOutputStream.println();
            LOG.error("Exception while copying from input to output stream: " + e.getMessage());
        } finally {
            lOutputStream.flush();
            lOutputStream.close();
        }
        // forward
        return null;
    }
}
