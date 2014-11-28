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

package de.conterra.smarteditor.controller;

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
 * @author kse
 *         Date: 17.03.2010
 *         Time: 09:29:08
 */
public class SaveLocalController implements Controller {

    private static Logger LOG = Logger.getLogger(SaveLocalController.class);

    private Map<String, String> addHeader;
    private Map<String, String> setHeader;
    private String contentType;

    protected BackendManagerService mBackendService;

    public BackendManagerService getBackendService() {
        return mBackendService;
    }

    public void setBackendService(BackendManagerService pBackendService) {
        mBackendService = pBackendService;
    }

    public Map<String, String> getAddHeader() {
        return addHeader;
    }

    public void setAddHeader(Map<String, String> addHeader) {
        this.addHeader = addHeader;
    }

    public Map<String, String> getSetHeader() {
        return setHeader;
    }

    public void setSetHeader(Map<String, String> setHeader) {
        this.setHeader = setHeader;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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
        lUtil.setContext(new EditorContext());
        String lFileId = lUtil.evaluateAsString("//gmd:fileIdentifier/gco:CharacterString/text()", lDoc);
        if(lFileId.equals("")){
        	lFileId=lUtil.evaluateAsString("//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:uniqueID']/sml:value/text()", lDoc);
        }
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
