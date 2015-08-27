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
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="editor" uri="http://www.52north.org/tags/editor" %>

<tiles:useAttribute name="cnt" id="smlClassification_cnt"/>

<editor:fragments modelAttribute="updateMetadata">
    <table class="marginLine">
        <tr>
            <td>
                <label for="smlClassification_${smlClassification_cnt}_Name" class="firstLabel">
                    <fmt:message key="element.smlTerm.name"/>
                </label></td><td>
                <form:input path="storage['smlClassification'].items[${smlClassification_cnt}].name" size="100"
                            id="smlClassification_${smlClassification_cnt}_Name"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="smlClassification_${smlClassification_cnt}_Definition" class="firstLabel">
                    <fmt:message key="element.smlTerm.definition"/>
                </label></td><td>
                <form:input path="storage['smlClassification'].items[${smlClassification_cnt}].definition" size="100"
                            id="smlClassification_${smlClassification_cnt}_Definition"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="smlClassification_${smlClassification_cnt}_Value" class="firstLabel">
                    <fmt:message key="element.smlTerm.value"/>
                </label></td><td>
                <form:input path="storage['smlClassification'].items[${smlClassification_cnt}].value" size="100"
                            id="smlClassification_${smlClassification_cnt}_Value"/>
            </td>
        </tr>
    </table>
</editor:fragments>



