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
package org.n52.smartsensoreditor.beans;

import org.w3c.dom.Document;

/**
 * Sores startup parameters
 * @author kse
 * Date: 26.03.2010
 * Time: 15:00:42
 */
public class StartEditorBean {

    private String mTarget;
    private String mResourceType;
    private String mTemplateName;
    private String mDraftName;
    private String mServiceUrl;
    private String mServiceName;
    private String mServiceType;
    private String mXslt;
    private Document mDocument;
    private String mFileIdentifier;
    //SOS
    private String mserviceTokenForSOS;
    private String mserviceProcedureIDForSOS;
    private String mserviceOperationForSOS;
    
    

    public String getFileIdentifier() {
        return mFileIdentifier;
    }

    public void setFileIdentifier(String pFileIdentifier) {
        mFileIdentifier = pFileIdentifier;
    }

    public String getXslt() {
        return mXslt;
    }

    public void setXslt(String pXslt) {
        mXslt = pXslt;
    }

    public Document getDocument() {
        return mDocument;
    }

    public void setDocument(Document pDocument) {
        mDocument = pDocument;
    }

    public String getTarget() {
        return mTarget;
    }

    public void setTarget(String pTarget) {
        mTarget = pTarget;
    }

    public String getResourceType() {
        return mResourceType;
    }

    public void setResourceType(String pResourceType) {
        mResourceType = pResourceType;
    }

    public String getTemplateName() {
        return mTemplateName;
    }

    public void setTemplateName(String pTemplateName) {
        mTemplateName = pTemplateName;
    }

    public String getDraftName() {
        return mDraftName;
    }

    public void setDraftName(String pDraftName) {
        mDraftName = pDraftName;
    }

    public String getServiceUrl() {
        return mServiceUrl;
    }

    public void setServiceUrl(String pServiceUrl) {
        mServiceUrl = pServiceUrl;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String pServiceName) {
        mServiceName = pServiceName;
    }

    public String getServiceType() {
        return mServiceType;
    }

    public void setServiceType(String pServiceType) {
        mServiceType = pServiceType;
    }
}
