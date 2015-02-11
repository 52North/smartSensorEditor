<%--

    See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    con terra GmbH licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License. You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="metadataElementTabs">
	<ul>
		<li><a href="#tabs-1"><fmt:message key="section.description" /></a></li>
<%-- 		<li><a href="#tabs-2"><fmt:message key="section.categories" /></a></li>
		<li><a href="#tabs-3"><fmt:message key="section.access" /></a></li>
		<li><a href="#tabs-4"><fmt:message key="section.distribution" /></a></li>
		<li><a href="#tabs-5"><fmt:message key="section.quality" /></a></li>
		<li><a href="#tabs-6"><fmt:message key="section.metadata" /></a></li> --%>
	</ul>
	<div id="tabs-1">
	
    <tiles:insertAttribute name="smlIdentifier" />
	<tiles:insertAttribute name="smlKeyword" />
	<tiles:insertAttribute name="smlIdentification" />
	
	<%-- 	<br>
		<tiles:insertAttribute name="alternateTitle" />
		<br>	    
		<tiles:insertAttribute name="abstract" />
		<br>
		<tiles:insertAttribute name="title" />
		<br>
		<tiles:insertAttribute name="browseGraphics" />
		<br>
		<tiles:insertAttribute name="resourceDate" />
		<br>
		<tiles:insertAttribute name="resourceParty" />
		<br>
		<tiles:insertAttribute name="referenceSystem" />
		<br>
		<tiles:insertAttribute name="geographicExtent" />
		<br>
		<tiles:insertAttribute name="geographicIdentifier" />
		<br>
		<tiles:insertAttribute name="temporalExtent" />
		<br>
		<tiles:insertAttribute name="spatialRepresentationType" />
		<br>
		<tiles:insertAttribute name="resourceLanguage" />
		<br>
		<tiles:insertAttribute name="resourceCharset" />
		<br>
		<tiles:insertAttribute name="resourceMaintenance" />
		<br>
		<tiles:insertAttribute name="resourceIdentifier" />
		<br>
	</div>
	<div id="tabs-2">
		<tiles:insertAttribute name="resourceType" />
		<br>
		<tiles:insertAttribute name="hierarchyLevelName" />
		<br>
		<tiles:insertAttribute name="descriptiveKeywords" />
		<br>
		<tiles:insertAttribute name="topicCategory" />
		<br>
	</div>
	<div id="tabs-3">
		<tiles:insertAttribute name="transferOptions" />
		<br>
		<tiles:insertAttribute name="useLimitations" />
		<br>
		<tiles:insertAttribute name="accessConstraints" />
		<br>
		<tiles:insertAttribute name="useConstraints" />
		<br>
		<tiles:insertAttribute name="otherConstraints" />
		<br>
		<tiles:insertAttribute name="classification" />
		<br>
	</div>
	<div id="tabs-4">
		<tiles:insertAttribute name="distributorFormat" />
		<br>
		<tiles:insertAttribute name="distributorParty" />
		<br>
	</div>
	<div id="tabs-5">
		<tiles:insertAttribute name="spatialResolution" />
		<br>
		<tiles:insertAttribute name="lineage" />
		<br>
		<tiles:insertAttribute name="conformity" />
	</div>
	<div id="tabs-6">
		<tiles:insertAttribute name="fileIdentifier" />
		<br>
		
		<tiles:insertAttribute name="parentIdentifier" />
		<br>
		<tiles:insertAttribute name="dateStamp" />
		<br>
		<tiles:insertAttribute name="metadataParty" />
		<br>
		<tiles:insertAttribute name="metadataCharacterSet" />
		<br>
		<tiles:insertAttribute name="metadataLanguage" />
		<br>
		<tiles:insertAttribute name="metadataStandard" />
		<br>
	</div> --%>
</div>

