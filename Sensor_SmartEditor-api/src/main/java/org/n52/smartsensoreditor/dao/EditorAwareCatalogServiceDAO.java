/**
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * con terra GmbH licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.n52.smartsensoreditor.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.w3c.dom.Document;

import de.conterra.smarteditor.cswclient.ext.header.PolicyMap;
import de.conterra.smarteditor.dao.CatalogServiceDAO;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;

/**
 * Access object to the catalog service using the globally defined editorContext
 * 
 * @author Jana
 */
public class EditorAwareCatalogServiceDAO extends CatalogServiceDAO {

	private EditorContext editorContext;

	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}

    /**
     * Return a list of existing draft names for the given user using the global editor context for evaluating XPaths
     *
     * @return Map consisting of <fileIdentifier, draftname> or null, if no draft is found
     */
	@Override
    public Map<String, String> getDraftNames()
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        Map<String, String> lMap = null;
        List<Document> lDraftRecords = this.getRecords("*",
                getMaxNumberOfDrafts(),
                "/filter/anyType.xml",
                PolicyMap.getActionString("discoveryDraft"));
        if (lDraftRecords != null) {
            lMap = new TreeMap<String, String>();
            // filter fileId and Title
            XPathUtil lPathUtil = new XPathUtil();
            lPathUtil.setContext(editorContext);
            for (Document lDocument : lDraftRecords) {
                String lFileId = lPathUtil.evaluateAsString("//gmd:fileIdentifier[1]/*/text()", lDocument);
                String lTitle = lPathUtil.evaluateAsString("//gmd:title[1]/*/text()", lDocument);
                lMap.put(lFileId, lTitle);
            }
        }
        return lMap;
    }


    
}
