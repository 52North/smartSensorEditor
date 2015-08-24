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
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<tiles:insertAttribute name="editor.header" />

<script type="text/javascript">
	$(document).ready(function() {
		// turn off exit confirmation
		setConfirmUnload(false);
		// remove edit session cookie
		$.cookie('tc_smartEditorSession', null);
	});
</script>

<div class="container">
	<fieldset>
		<legend>
			<fmt:message key="publish.finished.title" />
		</legend>

		<p>
			<fmt:message key="publish.finished.message" />
		</p>

		<p>&nbsp;</p>
		<fmt:message key="publish.finished.identifiers" />
		: <br>
		<p>
			<c:forEach items="${response.identifiers}" var="identifier">
				<c:out value="${identifier}" />
				<br>
			</c:forEach>
		</p>

		<p>&nbsp;</p>
		<fmt:message key="publish.finished.actions" />
		<br>

		<p>&nbsp;</p>
		<b><fmt:message key="publish.finished.inserted" /></b>:
		<c:out value="${response.totalInserted}" />
		<br> <b><fmt:message key="publish.finished.deleted" /></b>:
		<c:out value="${response.totalDeleted}" />
		<br> <b><fmt:message key="publish.finished.updated" /></b>:
		<c:out value="${response.totalUpdated}" />
		<br>
		<p>&nbsp;</p>
		<c:if test="${(not empty response)&&(response.error!='')})">
			<b><fmt:message key="published.finished.error" /></b>:<br>
			<span style="color: #FF0000"> <c:out value="${response.error}" />
			</span>
			<br>
		</c:if>
		<c:if test="${(not empty serverError) &&(serverError!='')}">
			<b><fmt:message key="published.finished.error" /></b>:<br>
			<span style="color: #FF0000"> <fmt:message
					key="${serverError}" />
			</span>
			<br>
		</c:if>
		<p>&nbsp;</p>
		<ul id="subMenu">
			<li><c:url value="/startEditor.do" var="startEditor" /> <a
				class="homepage" href='<c:out value="${startEditor}"/>'> <fmt:message
						key="action.home" />
			</a> <c:if
					test="${((not empty serverError) || (not empty response.error))&&(not empty sourcePage)}">
					<c:if
						test="${((serverError!='') || (response.error!='')) && (sourcePage=='publish')}">
						<c:url value="/edit.do" var="continueEditing" />
						<a href="<c:out value="${continueEditing}"/>"> <fmt:message
								key="publish.back" />
						</a>
					</c:if>
				</c:if></li>
		</ul>
	</fieldset>
</div>

<tiles:insertAttribute name="editor.footer" />
