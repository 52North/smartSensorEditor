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

<h2>
	<fmt:message key="start.new.title" />
</h2>

<p>&nbsp;</p>
<span> <fmt:message key="start.new.message" />
</span>

<script>
	$(function() {
		$("button").button();
		$('button[name="resourceTypeButton"]').click(function() {	 	
			$('#resourceType').val($(this).attr("id")); 
			//$('#startEditorBean').submit();
		});
		$("#dialog")
		.dialog(
				{
					autoOpen : false,
					modal : false,
					buttons : {
						'<fmt:message key="editor.general.widget.ok"/>' : function() {
								$(this).dialog('close');
							$('#resourceType').val($("#sensorSML_Profile").val());
							$('#startEditorBean').submit();
					
						}
					}
				});
	}); 
	function setSensorform(){
			if($('#resourceType').val()=="sensorProfile"){
				$("#dialog").dialog('open');
				return false;
			}
		}
</script>
<p>&nbsp;</p>
<form:form action="startNew.do" commandName="startEditorBean" id="startEditorBean"
	method="POST" onsubmit="return setSensorform()">
	<input type="hidden" id="resourceType" name="resourceType" value="" />
	<c:forEach items="${MD_ScopeCode.nvp}" var="entry"
		varStatus="rowCounter">
		<button style="width: 160px" id="${entry.value}"
			name="resourceTypeButton">${entry.name}</button>
		<c:if test="${rowCounter.count % 4 == 0}">
			<br>
		</c:if>
	</c:forEach>
</form:form>

<div id="dialog"
	title="<fmt:message key='start.new.dialog.sensor.title'/>">
	<p>
		<span class="ui-icon ui-icon-alert"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key='start.new.dialog.sensor.message' />
	</p>
	<p>
	 <select id="sensorSML_Profile">
      <c:forEach items="${sensorSMLProfile.nvp}" var="entry">
		 <option value="${entry.value}">${entry.name}</option>		
	</c:forEach>
    </select> 
    </p>
</div>