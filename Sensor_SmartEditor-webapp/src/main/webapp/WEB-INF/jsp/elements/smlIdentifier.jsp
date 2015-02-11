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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<table class="verticalTop">
    <tr>
        <td>
            <label for="smlIdentifier" id="smlIdentifierLabel" class="firstLabel width190 tooltip">
                <fmt:message key="element.smlIdentifier.label"/>
            </label>
        </td>
        <td>
            <b><c:out value="${updateMetadata.storage['smlIdentifier'].id}"/></b>
            <form:hidden path="storage['smlIdentifier'].id" id="smlIdentifier"/>
        </td>
    </tr>
</table>



