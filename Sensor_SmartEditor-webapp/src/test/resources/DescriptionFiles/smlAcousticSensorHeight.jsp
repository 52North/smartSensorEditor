<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<table class="verticalTop">
	<tr>
		<td><label for="smlAcousticSensorHeight" id="smlAcousticSensorHeight"
			class="firstLabel width190 tooltip"> <fmt:message
					key="element.smlAcousticSensorHeight.label" />
		</label></td>
		<td><form:input htmlEscape="true"
				path="storage['smlAcousticSensorHeight'].height" size="100"
				id="smlAcousticSensorHeight" /> <form:errors
				path="storage['smlAcousticSensorHeight'].height"
				cssClass="ui-state-error-text" /></td>
	</tr>
</table>
