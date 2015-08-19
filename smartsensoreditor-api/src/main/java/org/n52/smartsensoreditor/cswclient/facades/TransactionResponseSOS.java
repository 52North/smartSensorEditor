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
		Object o = evaluateXPath("//swes:assignedProcedure", XPathConstants.NODE);
		if(o!=null)
			insertedProcedure=String.valueOf(((Element) o).getFirstChild().getNodeValue());
		o = evaluateXPath("//swes:updatedProcedure", XPathConstants.NODE);
		if(o!=null)
			updatedProcedure=String.valueOf(((Element) o).getFirstChild().getNodeValue());
		o = evaluateXPath("//swes:deletedProcedure", XPathConstants.NODE);
		if(o!=null)
			deletedProcedure=String.valueOf(((Element) o).getFirstChild().getNodeValue());
	}

	/**
	 * Return a list of strings
	 *
	 * @return list of identifier or null
	 */
	@Override
	public List getIdentifiers() {
		List<String> lResult = new ArrayList<String>();
		if (insertedProcedure!=null){
			//assigned Procedure
			lResult.add("assigned Procedure: "+insertedProcedure);
			//assigned Offering
			//initialize the procedure ids
			Object o = evaluateXPath("//swes:assignedOffering", XPathConstants.NODE);
			if(o!=null){
				String insertedOffering=String.valueOf(((Element) o).getFirstChild().getNodeValue());
				lResult.add("assigned Offering: "+insertedOffering);
			}
		}else if(updatedProcedure!=null){
			lResult.add("updated Procedure: "+updatedProcedure);
		}else if(deletedProcedure!=null){
			lResult.add("deleted Procedure: "+deletedProcedure);
		}
		return lResult;
	}

	public String getRequestId() {
		if (insertedProcedure!=null){
			return insertedProcedure;
			
			}
	   if(updatedProcedure!=null){
			return updatedProcedure;
	   }
		if(deletedProcedure!=null){
			return deletedProcedure;
		}
		return "";
	}
	@Override
	public int getTotalInserted() {
		if (insertedProcedure == null) {
			LOG.debug("'//swes:assignedProcedure' results to NULL. Returning 0 thus.");
			return 0;
		}
		return 1;
	}
	@Override
	public int getTotalUpdated() {
		if (updatedProcedure == null) {
			LOG.debug("'//swes:updatedProcedure' results to NULL. Returning 0 thus.");
			return 0;
		}
		return 1;
	}
	@Override
	public int getTotalDeleted() {
		if (deletedProcedure == null) {
			LOG.debug("'//swes:deletedProcedure' results to NULL. Returning 0 thus.");
			return 0;
		}
		return 1;
	}
	@Override
	protected NamespaceContext getNamespaceContext() {
		return cswContextSOS;
	}
}
