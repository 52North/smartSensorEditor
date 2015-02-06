<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<table class="verticalTop">
	<tr>
		<td><label for="smlAcousticSensorWeight" id="smlAcousticSensorWeight"
			class="firstLabel width190 tooltip"> <fmt:message
					key="element.smlAcousticSensorWeight.label" />
		</label></td>
		<td><form:input htmlEscape="true"
				path="storage['smlAcousticSensorWeight'].weight" size="100"
				id="smlAcousticSensorWeight" /> <form:errors
				path="storage['smlAcousticSensorWeight'].weight"
				cssClass="ui-state-error-text" /></td>
	</tr>
</table>
