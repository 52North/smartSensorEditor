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

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<tiles:insertAttribute name="editor.header" />

<script type="text/javascript">
    $(document).ready(function() {
        setConfirmUnload(false); // turn off for this page
        $("#containerTabs").tabs({
            cookie: { name: "tc_smartEditorStart" }
        });
    });
</script>

<div style="font-size: large; color: blue">
	<center>
		<b>Sensor SmartEditor</b>
	</center>
</div>
<div id="showErrors"></div>
<div id="containerTabs">
	<ul>
		<li><a href="#tabs-new"><fmt:message key="start.new" /></a></li>
		<li><a href="#tabs-local"><fmt:message key="start.file" /></a></li>
		<li><a href="#tabs-template"><fmt:message
					key="start.template" /></a></li>
		<li><a href="#tabs-service"><fmt:message key="start.service" /></a></li>
	</ul>
	<div id="tabs-new">
		<tiles:insertAttribute name="new" />
	</div>
	<div id="tabs-local">
		<tiles:insertAttribute name="local" />
	</div>
	<div id="tabs-template">
		<tiles:insertAttribute name="template" />
	</div>
	<div id="tabs-service">
		<tiles:insertAttribute name="service" />
	</div>
</div>

<tiles:insertAttribute name="editor.footer" />