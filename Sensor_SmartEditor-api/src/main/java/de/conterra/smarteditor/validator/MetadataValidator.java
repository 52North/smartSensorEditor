/**
 * Copyright (C) ${inceptionYear}-2014 52°North Initiative for Geospatial Open Source
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
package de.conterra.smarteditor.validator;

import java.io.StringWriter;

import de.conterra.smarteditor.beans.BackendBean;
import de.conterra.smarteditor.service.BackendManagerService;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;

/**
 * Main validator class for validation
 * <p/>
 * 
 * @author kse Date: 27.02.2010 Time: 18:05:46
 */
public class MetadataValidator implements Validator {

	private BackendManagerService mService;
	static private Logger LOG = Logger.getLogger(MetadataValidator.class);

	public BackendManagerService getService() {
		return mService;
	}

	public void setService(BackendManagerService pService) {
		mService = pService;
	}

	public boolean supports(Class clazz) {
		return BackendBean.class.isAssignableFrom(clazz);
	}

	/**
	 * validates the backend bean properties against a given schematron (or
	 * else) validator
	 *
	 * @param target
	 * @param errors
	 */
	public void validate(Object target, Errors errors) {
		BackendBean lBean = (BackendBean) target;
		// chekc if we need to validate
		if (lBean.getValidatorId() != null
				&& !lBean.getValidatorId().equals("")) {
			// apply schematron transformation
			Document lReport = mService.validate(lBean.getValidatorId());
			LOG.debug("lReport from validation: "+DOMUtil.convertToString(lReport, true));
			// add assertions to errors.
			XPathUtil lUtil = new XPathUtil();
			lUtil.setContext(new EditorContext());
			Object o = lUtil.evaluateXPath("//svrl:failed-assert",
					XPathConstants.NODESET, lReport);
			if (o != null) {
				NodeList lList = ((NodeList) o);
				if (lList.getLength() > 0) {
					LOG.debug("There are validation errors: "
							+ lList.toString());
				} else {
					LOG.debug("There are no validation errors");
				}
				for (int i = 0; i < lList.getLength(); i++) {
					Node lNode = lList.item(i);
					LOG.debug("error: " + lList.item(i));
					errors.rejectValue(lUtil.evaluateAsString("@id", lNode),
							lUtil.evaluateAsString("svrl:text", lNode));
				}
			}
		}
	}
}
