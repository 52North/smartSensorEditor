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

import de.conterra.smarteditor.beans.IConfigOptions;
import de.conterra.smarteditor.beans.UserInfoBean;
import de.conterra.smarteditor.common.workflow.IState;
import de.conterra.smarteditor.common.workflow.IWorkflowManager;
import de.conterra.smarteditor.cswclient.exception.SystemException;
import de.conterra.smarteditor.cswclient.ext.facades.ExtGetRecordByIdResponse;
import de.conterra.smarteditor.cswclient.ext.facades.ExtGetRecordsResponse;
import de.conterra.smarteditor.cswclient.ext.header.Owner;
import de.conterra.smarteditor.cswclient.ext.header.PolicyMap;
import de.conterra.smarteditor.cswclient.ext.header.TcRecord;
import de.conterra.smarteditor.cswclient.ext.invoker.ExtHttpSoapInvoker;
import de.conterra.smarteditor.cswclient.ext.request.ExtGetRecordByIdRequest;
import de.conterra.smarteditor.cswclient.ext.request.ExtGetRecordsRequest;
import de.conterra.smarteditor.cswclient.ext.request.ExtTransactionRequest;
import de.conterra.smarteditor.cswclient.ext.request.SecurityInfoImpl;
import de.conterra.smarteditor.cswclient.facades.GetRecordByIdResponse;
import de.conterra.smarteditor.cswclient.facades.GetRecordsResponse;
import de.conterra.smarteditor.cswclient.facades.IFacade;
import de.conterra.smarteditor.cswclient.facades.TransactionResponse;
import de.conterra.smarteditor.cswclient.invoker.HttpSoapInvoker;
import de.conterra.smarteditor.cswclient.invoker.IServiceInvoker;
import de.conterra.smarteditor.cswclient.request.*;
import de.conterra.smarteditor.cswclient.util.Defaults;
import de.conterra.smarteditor.dao.CatalogServiceDAO;
import de.conterra.smarteditor.util.DOMUtil;
import de.conterra.smarteditor.util.XPathUtil;
import de.conterra.smarteditor.xml.EditorContext;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Access object to the catalog service
 * <p/>
 * @author kse
 * Date: 23.02.2010
 * Time: 12:17:47
 */
@Deprecated
public class CatalogServiceDAOSML extends CatalogServiceDAO {

    protected static final Logger LOG = Logger.getLogger(CatalogServiceDAO.class);
    private int mMaxNumberOfDrafts;

    private IConfigOptions mConfig;
    private UserInfoBean mUserInfo;

    private IWorkflowManager mManager;

    private String mDiscoverySoapEndpoint;
    private String mManagerSoapEndpoint;
    private Owner mOwner; // latest owner of the metadata set to be updated
    private String mOutputSchema;           // defines the schema that is requested from the catalog
	private EditorContext editorContext;



	public EditorContext getEditorContext() {
		return editorContext;
	}

	public void setEditorContext(EditorContext editorContext) {
		this.editorContext = editorContext;
	}
    public String getOutputSchema() {
        return mOutputSchema;
    }

    public void setOutputSchema(String pOutputSchema) {
        mOutputSchema = pOutputSchema;
    }

    public IConfigOptions getConfig() {
        return mConfig;
    }

    public void setConfig(IConfigOptions pConfig) {
        mConfig = pConfig;
    }

    public UserInfoBean getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfoBean pUserInfo) {
        mUserInfo = pUserInfo;
    }

    public String getDiscoverySoapEndpoint() {
        return mDiscoverySoapEndpoint;
    }

    public void setDiscoverySoapEndpoint(String pDiscoverySoapEndpoint) {
        mDiscoverySoapEndpoint = pDiscoverySoapEndpoint;
    }

    public String getManagerSoapEndpoint() {
        return mManagerSoapEndpoint;
    }

    public void setManagerSoapEndpoint(String pManagerSoapEndpoint) {
        mManagerSoapEndpoint = pManagerSoapEndpoint;
    }

    public int getMaxNumberOfDrafts() {
        return mMaxNumberOfDrafts;
    }

    public void setMaxNumberOfDrafts(int pMaxNumberOfDrafts) {
        mMaxNumberOfDrafts = pMaxNumberOfDrafts;
    }

    public IWorkflowManager getManager() {
        return mManager;
    }

    public void setManager(IWorkflowManager pManager) {
        mManager = pManager;
    }

    /**
     * Get records from a CSW
     *
     * @param pSearchTerm   anytext to look for
     * @param pMaxRecords   number of max records to return
     * @param pFilterId     associated filterid
     * @param pActionString action string
     * @return List<Document> of records matched or null
     * @throws de.conterra.smarteditor.cswclient.exception.InvokerException
     *
     * @throws java.rmi.RemoteException
     */
    public List<Document> getRecords(String pSearchTerm,
                                     int pMaxRecords,
                                     String pFilterId,
                                     String pActionString)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        // get filter resource
        Document lFilter = null;
        ResourceLoader lLoader = new DefaultResourceLoader();
        Resource lRes = lLoader.getResource(pFilterId);
        try {
            String str = convertStreamtoString(lRes.getInputStream());
            lFilter = DOMUtil.createFromString(str.replaceAll("@ANYTEXT@", pSearchTerm), true);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        // prepare request
        GetRecordsRequest lRequest = new GetRecordsRequest();
        lRequest.setStartPosition(1)
                .setMaxRecords(pMaxRecords)
                .setOutputSchema(Defaults.OUTPUTSCHEMA_GMD)
                .setTypeName(Defaults.TYPENAME_GMD)
                .setElementSetName(Defaults.ELEMENTSETNAME_FULL)
                .setFilter(lFilter)
                .setResultType(Defaults.RESULTTYPE_RESULTS);
        lRequest.addNamespace("apiso", Defaults.NAMESPACE_APISO);
        lRequest.addNamespace("gmd", Defaults.NAMESPACE_GMD);
        IServiceInvoker lInvoker = null;

        if (getUserInfo() != null && getUserInfo().getTicket() != null) {
            ExtGetRecordsRequest lExtRequest = new ExtGetRecordsRequest(lRequest);
            // wrap with security
            SecurityInfoImpl lInfo = new SecurityInfoImpl(getUserInfo().getTicket(), pActionString);
            lExtRequest.setSecurityInfo(lInfo);
            // initiate facae
            ExtGetRecordsResponse lFacade = new ExtGetRecordsResponse(new GetRecordsResponse());
            // intantiate invoker
            lInvoker = new ExtHttpSoapInvoker();
            if (getDiscoverySoapEndpoint()== null || getDiscoverySoapEndpoint().equals("")) {
                lInvoker.initialize(getConfig().getCswEndpoint("discovery"));
            } else {
                lInvoker.initialize(getDiscoverySoapEndpoint());
            }
            lInvoker.invoke(lExtRequest, lFacade);
            return lFacade.getRecords();
        } else {
            GetRecordsResponse lFacade = new GetRecordsResponse();
            lInvoker = new HttpSoapInvoker();
            if (getDiscoverySoapEndpoint()== null || getDiscoverySoapEndpoint().equals("")) {
                lInvoker.initialize(getConfig().getCswEndpoint("discovery"));
            } else {
                lInvoker.initialize(getDiscoverySoapEndpoint());
            }
            lInvoker.invoke(lRequest, lFacade);
            return lFacade.getRecords();
        }
    }


    /**
     * Retrieves a document by identifier form the backend catalogue service
     *
     * @param pFileId
     * @return Catalog response with document
     */
    public IFacade getDocumentByIdentifier(String pFileId, String pActionString)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        List lFileId = new ArrayList();
        lFileId.add(pFileId);
        // build request
        GetRecordByIdRequest lRequest = new GetRecordByIdRequest();
        lRequest.setElementSetName(Defaults.ELEMENTSETNAME_FULL)
                .setIdentifier(lFileId).setOutputSchema(mOutputSchema);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Output schema is: " + lRequest.getOutputSchema());
            LOG.debug("Identifier is: " + lRequest.getIdentifier());
        }
        // check if we have a user ticket
        if (getUserInfo() != null && getUserInfo().getTicket() != null) {
            // get decorator for header information
            ExtGetRecordByIdRequest lExtRequest = new ExtGetRecordByIdRequest(lRequest);
            // build security info
            String lAction = pActionString;
            if (lAction == null) {
                lAction = PolicyMap.getActionString("discoveryRead");
            }
            SecurityInfoImpl lSecImpl = new SecurityInfoImpl(getUserInfo().getTicket(), lAction);
            lExtRequest.setSecurityInfo(lSecImpl);
            // instantiate invoker
            IServiceInvoker lInvoker = new ExtHttpSoapInvoker();
            //initialize invoker
            if (getDiscoverySoapEndpoint()== null || getDiscoverySoapEndpoint().equals("")) {
                lInvoker.initialize(getConfig().getCswEndpoint("discovery"));
            } else {
                lInvoker.initialize(getDiscoverySoapEndpoint());
            }
            // instantiate facade
            ExtGetRecordByIdResponse lFacade = new ExtGetRecordByIdResponse(new GetRecordByIdResponse());
            // process request
            // invoke request
            try {
                lInvoker.invoke(lExtRequest, lFacade);
            } catch (SystemException e) {
                LOG.warn("Catalog threw the following exception:" + e.getMessage());
                return null;
            } catch (RuntimeException e) {
                LOG.warn("Catalog threw the following exception:" + e.getMessage());
                return null;
            }
            // get resulting records
            List lRecords = lFacade.getRecords();
            if (lRecords == null || lRecords.size() == 0) {
                LOG.info("No record has been found!");
                return null;
            }
            // get ownership
            TcRecord[] lTcRecordArray = lFacade.getTcRecords();
            if (mOwner == null) {
                mOwner = new Owner();
            }
            if (lTcRecordArray != null && lTcRecordArray.length == 1) {
                mOwner.setGroupID(lTcRecordArray[0].getOwner().getGroupID());
                mOwner.setProtection(lTcRecordArray[0].getOwner().getProtection());
                mOwner.setUserID(lTcRecordArray[0].getOwner().getUserID());
                mOwner.setStatus(lTcRecordArray[0].getOwner().getStatus());
            } else {
                LOG.warn("No ownership information found for MD with ID '" + pFileId + "'.");
            }
            return lFacade;
        } else {
            IServiceInvoker lInvoker = new HttpSoapInvoker();
            //initialize invoker
            if (getDiscoverySoapEndpoint()== null || getDiscoverySoapEndpoint().equals("")) {
                lInvoker.initialize(getConfig().getCswEndpoint("discovery"));
            } else {
                lInvoker.initialize(getDiscoverySoapEndpoint());
            }
            // instantiate facade
            GetRecordByIdResponse lFacade = new GetRecordByIdResponse();
            try {
                lInvoker.invoke(lRequest, lFacade);
            } catch (SystemException e) {
                LOG.warn("Catalog threw the following exception:" + e.getMessage());
                return null;
            } catch (RuntimeException e) {
                LOG.warn("Catalog threw the following exception:" + e.getMessage());
                return null;
            }
            return lFacade;
        }
    }

    /**
     * Inserts a document to the catalog service
     *
     * @param pDoc    document to publish
     * @param pStatus status id for the document
     * @return Transaction response
     */
    public TransactionResponse insert(Document pDoc,
                                      String pStatus)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        AbstractStatement lStatement;
        lStatement = new InsertStatement();
        List lRecords = new ArrayList();
        lRecords.add(pDoc);
        ((InsertStatement) lStatement).setRecords(lRecords);
        return this.doTransaction(lStatement, pStatus, PolicyMap.getActionString("transaction"));
    }

    /**
     * @param pDoc
     * @return
     * @throws de.conterra.smarteditor.cswclient.exception.InvokerException
     *
     * @throws java.rmi.RemoteException
     */
    public TransactionResponse insertDraft(Document pDoc)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        AbstractStatement lStatement;
        lStatement = new InsertStatement();
        List lRecords = new ArrayList();
        lRecords.add(pDoc);
        ((InsertStatement) lStatement).setRecords(lRecords);
        return this.doTransaction(lStatement, "draft", PolicyMap.getActionString("transactionDraft"));
    }


    /**
     * updates an existing draft
     *
     * @param pDoc
     * @return
     * @throws de.conterra.smarteditor.cswclient.exception.InvokerException
     *
     * @throws java.rmi.RemoteException
     */
    public TransactionResponse updateDraft(Document pDoc)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        AbstractStatement lStatement = new UpdateStatement();
        List lRecords = new ArrayList();
        lRecords.add(pDoc);
        ((UpdateStatement) lStatement).setRecord(pDoc);
        return this.doTransaction(lStatement, "draft", PolicyMap.getActionString("transactionDraft"));
    }

    /**
     * Checks if a draft with the given title exists in the catalog
     *
     * @param pTitle draft title
     * @return true if yes otherwise false
     */
    public boolean draftExists(String pTitle) {
        // determine title
        try {
            Map<String, String> lMap = this.getDraftNames();
            return lMap != null && lMap.containsValue(pTitle);
        } catch (RemoteException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * @param pFileIdentifier
     * @return
     * @throws de.conterra.smarteditor.cswclient.exception.InvokerException
     *
     * @throws java.rmi.RemoteException
     */
    public Document getDraft(String pFileIdentifier)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        IFacade lResponse = this.getDocumentByIdentifier(pFileIdentifier, PolicyMap.getActionString("discoveryDraft"));
        if (lResponse == null) {
            LOG.warn("Did not find a draft with id " + pFileIdentifier);
            return null;
        }
        if (lResponse instanceof ExtGetRecordByIdResponse) {
            return (Document) ((ExtGetRecordByIdResponse) lResponse).getRecords().get(0);
        } else {
            return (Document) ((GetRecordByIdResponse) lResponse).getRecords().get(0);
        }
    }

    /**
     * Deletes a draft given by identifier
     *
     * @param pFileId draft identifier
     * @return
     * @throws de.conterra.smarteditor.cswclient.exception.InvokerException
     *
     * @throws java.rmi.RemoteException
     */
    public TransactionResponse deleteDraft(String pFileId)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        DeleteStatement lStatement = new DeleteStatement();
        Document lFilter = null;
        ResourceLoader lLoader = new DefaultResourceLoader();
        Resource lRes = lLoader.getResource("/filter/fileIdentifier.xml");
        try {
            String str = convertStreamtoString(lRes.getInputStream());
            lFilter = DOMUtil.createFromString(str.replaceAll("@ID@", pFileId), true);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        lStatement.setConstraint(lFilter);
        return this.doTransaction(lStatement, null, PolicyMap.getActionString("transactionDraft"));
    }


    /**
     * @param pStatus
     * @return
     */
    public TransactionResponse doTransaction(AbstractStatement pStatement,
                                             String pStatus,
                                             String pActionString)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        // create transaction request
        TransactionRequest lRequest = new TransactionRequest();
        lRequest.addStatement(pStatement);
        // create security decorator
        ExtTransactionRequest lExtRequest = new ExtTransactionRequest(lRequest);
        SecurityInfoImpl lSec = new SecurityInfoImpl(getUserInfo().getTicket(), pActionString);
        // set owner information
        if (getUserInfo().getOwner() != null) {
            LOG.info("Setting ownership from owner object");
            lSec.setGroupID(getUserInfo().getOwner().getGroupID());
            lSec.setUserID(getUserInfo().getOwner().getUserID());
        } else {
            LOG.info("Setting ownership from SAML ticket");
            lSec.setGroupID(getUserInfo().getGroupId());
            lSec.setUserID(getUserInfo().getUserId());
        }
        // get protection level for state
        if (pStatus != null) {
            IState lState = (IState)mManager.getState(pStatus, null);
            lSec.setProtectionLevel(lState.getProtectionLevel());
            lSec.setStatus(pStatus);
        }
        lExtRequest.setSecurityInfo(lSec);
        // create and initialize invoker
        IServiceInvoker lInvoker = new ExtHttpSoapInvoker();
        if (getManagerSoapEndpoint() == null || getManagerSoapEndpoint().equals("")) {
            lInvoker.initialize(getConfig().getCswEndpoint("transaction"));
        } else {
            lInvoker.initialize(getManagerSoapEndpoint());
        }

        // create transaction facade
        TransactionResponse lFacade = new TransactionResponse();
        // invoke request
        lInvoker.invoke(lExtRequest, lFacade);
        LOG.info("Finished transaction.");
        return lFacade;

    }

    /**
     * Update a document to the catalog service
     *
     * @param pDoc    document to publish
     * @param pStatus status id for the document
     * @return Transaction response
     */
    public TransactionResponse update(Document pDoc,
                                      String pStatus)
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        AbstractStatement lStatement = new UpdateStatement();
        ((UpdateStatement) lStatement).setRecord(pDoc);
        getUserInfo().setUpdate(false);
        return this.doTransaction(lStatement, pStatus, PolicyMap.getActionString("transaction"));
    }


    /**
     * Return a list of existing draft names for the given user
     *
     * @return Map consisting of <fileIdentifier, draftname> or null, if no draft is found
     */
    public Map<String, String> getDraftNames()
            throws de.conterra.smarteditor.cswclient.exception.InvokerException, java.rmi.RemoteException {
        Map<String, String> lMap = null;
        List<Document> lDraftRecords = this.getRecords("*",
                mMaxNumberOfDrafts,
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


    /**
     * Determines the current status of the document
     *
     * @param pFileId
     * @return Statusid or null
     */
    public String getStatus(String pFileId) {
        try {
            IFacade lResponse = getDocumentByIdentifier(pFileId, null);
            if (lResponse instanceof ExtGetRecordByIdResponse) {
                TcRecord[] lTcRecordArray = ((ExtGetRecordByIdResponse) lResponse).getTcRecords();
                if (lTcRecordArray != null && lTcRecordArray.length == 1) {
                    return lTcRecordArray[0].getOwner().getStatus();
                }
            }
        } catch (RemoteException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
        return null;
    }

    /**
     * @param io
     * @return
     */

    private String convertStreamtoString(InputStream io) {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(io, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                io.close();
            }
            return sb.toString();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
