<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<table class="verticalTop">
	<tr>
		<td><label for="smlAcousticSensorLength" id="smlAcousticSensorLength"
			class="firstLabel width190 tooltip"> <fmt:message
					key="element.smlAcousticSensorLength.label" />
		</label></td>
		<td><form:input htmlEscape="true"
				path="storage['smlAcousticSensorLength'].length" size="100"
				id="smlAcousticSensorLength" /> <form:errors
				path="storage['smlAcousticSensorLength'].length"
				cssClass="ui-state-error-text" /></td>
	</tr>
</table>
