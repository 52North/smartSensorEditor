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
    private String serviceProcedureIDForSOS;
    private String serviceOperationForSOS;
    
    

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
