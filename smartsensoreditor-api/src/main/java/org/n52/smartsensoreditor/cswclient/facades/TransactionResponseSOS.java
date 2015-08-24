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
package org.n52.smartsensoreditor.cswclient.facades;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import de.conterra.smarteditor.cswclient.facades.TransactionResponse;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Response facade for a Transaction response
 *
 */
public class TransactionResponseSOS extends TransactionResponse {

	private static Logger LOG = Logger.getLogger(TransactionResponseSOS.class);
	protected final static CSWContextSOS cswContextSOS = new CSWContextSOS();

	private String insertedProcedure;
	private String updatedProcedure;
	private String deletedProcedure;
	private String error;

	public TransactionResponseSOS() {
		super(null);
	}

	/**
	 * Class contructor with parameters
	 *
	 * @param pDoc original server response
	 */
	public TransactionResponseSOS(Document pDoc) {
		super(pDoc);
		//initialize the procedure ids
		insertedProcedure=evaluateAsString("//swes:assignedProcedure");
		updatedProcedure=evaluateAsString("//swes:updatedProcedure");
        deletedProcedure=evaluateAsString("//swes:deletedProcedure");
	}

	/**
	 * Return a list of strings
	 *
	 * @return list of identifier or null
	 */
	@Override
	public List getIdentifiers() {
		List<String> lResult = new ArrayList<String>();
		if (!insertedProcedure.equals("")){
			//assigned Procedure
			lResult.add(insertedProcedure);
			//assigned Offering
			Element o = (Element)evaluateXPath("//swes:assignedOffering", XPathConstants.NODE);
			if(o!=null){
				NodeList list=o.getChildNodes();
				int length=list.getLength();
				Node node;
				for(int i=0; i<length;i++){
					node=list.item(i);
					String insertedOffering=node.getNodeValue();
					lResult.add(insertedOffering);
				}
			}
		}else if(!updatedProcedure.equals("")){
			lResult.add(updatedProcedure);
		}else if(!deletedProcedure.equals("")){
			lResult.add(deletedProcedure);
		}
		return lResult;
	}

	public String getRequestId() {
		if (!insertedProcedure.equals("")){
			return insertedProcedure;
			
			}
	   if(!updatedProcedure.equals("")){
			return updatedProcedure;
	   }
		if(!deletedProcedure.equals("")){
			return deletedProcedure;
		}
		return "";
	}
	@Override
	public int getTotalInserted() {
		if (insertedProcedure.equals("")) {
			LOG.debug("'//swes:assignedProcedure' results to NULL. Returning 0 thus.");
			return 0;
		}
		return 1;
	}
	@Override
	public int getTotalUpdated() {
		if (updatedProcedure.equals("")) {
			LOG.debug("'//swes:updatedProcedure' results to NULL. Returning 0 thus.");
			return 0;
		}
		return 1;
	}
	@Override
	public int getTotalDeleted() {
		if (deletedProcedure.equals("")) {
			LOG.debug("'//swes:deletedProcedure' results to NULL. Returning 0 thus.");
			return 0;
		}
		return 1;
	}
	
	public String getError() {
		error=evaluateAsString("//ows:ExceptionText");
		if (error.equals("")) {
			LOG.debug("'//ows:ExceptionText' results to NULL. Returning 0 thus.");
			error="";
		}
		return error;
	}
	public void setError(String error){
		this.error=error;
	}
	
	@Override
	protected NamespaceContext getNamespaceContext() {
		return cswContextSOS;
	}
}
