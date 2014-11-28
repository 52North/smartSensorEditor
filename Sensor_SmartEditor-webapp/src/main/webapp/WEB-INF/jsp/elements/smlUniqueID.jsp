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

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<table class="verticalTop">
    <tr>
        <td>
            <label for="resTitle" id="smlUniqueID" class="firstLabel width190 tooltip">
                <fmt:message key="element.smlUniqueID.label"/>
            </label>
        </td>
        <td>
            <form:input htmlEscape="true" path="storage['smlUniqueID'].uniqueID" size="100" id="resTitle"/><br>
            <form:errors path="storage['smlUniqueID'].uniqueID" cssClass="ui-state-error-text"/>
        </td>
    </tr>
</table>



