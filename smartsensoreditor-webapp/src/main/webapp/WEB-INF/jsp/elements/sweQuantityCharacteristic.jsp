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

<tiles:useAttribute name="cnt" id="sweQuantityCharacteristic_cnt"/>

<editor:fragments modelAttribute="updateMetadata">
    <table class="marginLine">
        <tr>
            <td>
                <label for="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Identifier" class="firstLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.identifier"/>
                </label></td><td>
                <form:input path="storage['sweQuantityCharacteristic'].items[${sweQuantityCharacteristic_cnt}].identifier" size="100"
                            id="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Identifier"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Definition" class="firstLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.definition"/>
                </label></td><td>
                <form:input path="storage['sweQuantityCharacteristic'].items[${sweQuantityCharacteristic_cnt}].definition" size="100"
                            id="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Definition"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Value" class="firstLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.value"/>
                </label></td><td>
                <form:input path="storage['sweQuantityCharacteristic'].items[${sweQuantityCharacteristic_cnt}].value" size="100"
                            id="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Value"/>
            </td>
        </tr>
             <tr>
            <td>
                <label for="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Description" class="firstLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.description"/>
                </label></td><td>
                <form:input path="storage['sweQuantityCharacteristic'].items[${sweQuantityCharacteristic_cnt}].description" size="100"
                            id="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Description"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Uom" class="firstLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.uom"/>
                </label></td><td>
                <form:input path="storage['sweQuantityCharacteristic'].items[${sweQuantityCharacteristic_cnt}].uom" size="100"
                            id="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Uom"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Label" class="firstLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.labelElement"/>
                </label></td><td>
                <form:input path="storage['sweQuantityCharacteristic'].items[${sweQuantityCharacteristic_cnt}].label" size="100"
                            id="sweQuantityCharacteristic_${sweQuantityCharacteristic_cnt}_Label"/>
            </td>
        </tr>
    </table>
</editor:fragments>



