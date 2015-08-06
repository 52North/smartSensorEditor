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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(document).ready(
			function() {
				// trigger visibility of service Name
				$('#serviceNameContainer').hide();
				$('#serviceTokenForSOS').hide();
				$('#DIVserviceOperationForSOS').hide();
				$('#serviceProcedureIDForSOS').hide();
				$('#serviceType').click(function() {
					if ($('#serviceType').attr('value') == 'ARCIMS') {
						$('#serviceNameContainer').show();
					} else {
						$('#serviceNameContainer').hide();
					}
					if ($('#serviceType').attr('value') == 'SOS') {
						$('#serviceTokenForSOS').show();
						$('#DIVserviceOperationForSOS').show();
						$('#serviceProcedureIDForSOS').show();
					} else {
						$('#serviceTokenForSOS').hide();
						$('#DIVserviceOperationForSOS').hide();
						$('#serviceProcedureIDForSOS').hide();
						$('#serviceProcedureIDForSOS').hide();
					}
				});

			});
</script>

<h2>
	<fmt:message key="start.service.title" />
</h2>

<p>&nbsp;</p>
<span> <fmt:message key="start.service.message" />
</span>

<p>&nbsp;</p>
<form:form action="startServiceSOS.do" commandName="startEditorBeanSML"
	method="Post">
	<div id="serviceUrlContainer" class="serviceDivSOS">
		<label for="serviceUrl" class="firstLabel width150"><fmt:message
				key="start.service.url" /></label> <input name="serviceUrl" id="serviceUrl"
			size="100" /> <br>
		<form:errors path="serviceUrl" cssClass="ui-state-error-text" />
	</div>
	<div id="serviceNameContainer" class="serviceDivSOS">
		<label for="serviceName" class="firstLabel width150"><fmt:message
				key="start.service.name" /></label> <input name="serviceName"
			id="serviceName" size="100" />
	</div>

	<div id="serviceTokenForSOS" class="serviceDivSOS">
		<label for="serviceTokenforSOS" class="firstLabel width150"><fmt:message
				key="start.service.tokenForSOS" /></label> <input name="serviceTokenForSOS"
			id="serviceTokenForSOS" size="100" /><br>
		<form:errors path="serviceTokenForSOS" cssClass="ui-state-error-text" />
	</div>

	<div id="serviceProcedureIDForSOS" class="serviceDivSOS">
		<label for="serviceProcedureIDForSOS" class="firstLabel width150"><fmt:message
				key="start.service.procedureIDForSOS" /></label> <input
			name="serviceProcedureIDForSOS" id="serviceProcedureIDForSOS"
			size="100" /><br>
		<form:errors path="serviceProcedureIDForSOS"
			cssClass="ui-state-error-text" />
	</div>


	<div id="DIVserviceOperationForSOS" class="serviceDivSOS">
		<label for="serviceOperationForSOS" class="firstLabel width150"><fmt:message
				key="start.service.operationForSOS" /></label> <select
			id="serviceOperationForSOS" name="serviceOperationForSOS">
			<option value=""><fmt:message key="editor.general.choose" /></option>
			<c:forEach items="${SOS_Operations.nvp}" var="entry">
				<option value="${entry.value}">${entry.name}</option>
			</c:forEach>
		</select><br>
		<form:errors path="serviceOperationForSOS"
			cssClass="ui-state-error-text" />
	</div>


	<div id="DIVserviceType" class="serviceDivSOS">
		<label for="serviceType" class="firstLabel width150"><fmt:message
				key="start.service.type" /></label> <select id="serviceType"
			name="serviceType">
			<option value=""><fmt:message key="editor.general.choose" /></option>
			<c:forEach items="${CT_ServiceTypeExt.nvp}" var="entry">
				<option value="${entry.value}">${entry.name}</option>
			</c:forEach>
		</select><br>
		<form:errors path="serviceType" cssClass="ui-state-error-text" />
	</div>


	<br>

	<p>&nbsp;</p>
	<input id="serviceSubmitButton" class="button" type="submit"
		value="<fmt:message key="start.submit"/>"
		title="<fmt:message key="start.submit"/>">
</form:form>

