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

import de.conterra.smarteditor.beans.PublishBean;

/**
 * [Explain the purpose of this class or interface, dude]
 * 
 *
 */
public class PublishBeanSML extends PublishBean {

	private String mServiceUrlSOS;
	private String mServiceTokenSOS;
	private String mProcedureIdSOS;
	private String mServiceOperationSOS;
	private String mSwesObservableProperties;
	private String mSOSObservationTypes;
	private String mSOSFeatureOfInterestTypes;
	
	public String getServiceUrlSOS() {
		return mServiceUrlSOS;
	}
	public void setServiceUrlSOS(String mServiceUrlSOS) {
		this.mServiceUrlSOS = mServiceUrlSOS;
	}
	public String getServiceTokenSOS() {
		return mServiceTokenSOS;
	}
	public void setServiceTokenSOS(String mServiceTokenSOS) {
		this.mServiceTokenSOS = mServiceTokenSOS;
	}
	public String getProcedureIdSOS() {
		return mProcedureIdSOS;
	}
	public void setProcedureIdSOS(String mProcedureIdSOS) {
		this.mProcedureIdSOS = mProcedureIdSOS;
	}
	public String getServiceOperationSOS() {
		return mServiceOperationSOS;
	}
	public void setServiceOperationSOS(String mServiceOperationSOS) {
		this.mServiceOperationSOS = mServiceOperationSOS;
	}

	public String getSwesObservableProperties() {
		return mSwesObservableProperties;
	}
	public void setSwesObservableProperties(String mSwesObservableProperties) {
		this.mSwesObservableProperties = mSwesObservableProperties;
	}
	public String getSosObservationTypes() {
		return mSOSObservationTypes;
	}
	public void setSosObservationTypes(String sosObservationTypes) {
		this.mSOSObservationTypes = sosObservationTypes;
	}
	public String getSosFeatureOfInterestTypes() {
		return mSOSFeatureOfInterestTypes;
	}
	public void setSosFeatureOfInterestTypes(String sosFeatureOfInterestTypes) {
		this.mSOSFeatureOfInterestTypes = sosFeatureOfInterestTypes;
	}

}
