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
	<li><a href="#tabs-2"><fmt:message key="section.characteristics" /></a></li>
<%-- 			<li><a href="#tabs-3"><fmt:message key="section.access" /></a></li>
		<li><a href="#tabs-4"><fmt:message key="section.distribution" /></a></li>
		<li><a href="#tabs-5"><fmt:message key="section.quality" /></a></li>
		<li><a href="#tabs-6"><fmt:message key="section.metadata" /></a></li> --%>
	</ul>
	<div id="tabs-1">
	
    <tiles:insertAttribute name="smlIdentifier" />
	<tiles:insertAttribute name="smlKeyword" />
	<tiles:insertAttribute name="smlIdentification" />
	
	
	</div>
	
	<div id="tabs-2">
		<tiles:insertAttribute name="sweQuantityCharacteristic" />
		<br>
	</div>

</div>

