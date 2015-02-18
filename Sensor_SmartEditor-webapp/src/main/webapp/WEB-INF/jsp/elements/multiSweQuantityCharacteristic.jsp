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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<script type="text/javascript">
    $(document).ready(function () {
        // setup tabs
        var $sweQuantityCharacteristicTabs = $('#sweQuantityCharacteristicTabs').tabs();
        // select last tab that has been added
        $sweQuantityCharacteristicTabs.tabs('select', $sweQuantityCharacteristicTabs.tabs('length') - 1);
    });
    Spring.addDecoration(new SmartEditor.AjaxEventDecoration({
        elementId: "addsweQuantityCharacteristicButton",
        event: "onclick",
        formId: "updateMetadata",
        params: {fragments: "sweQuantityCharacteristic",
            method: "add",
            elementName: "sweQuantityCharacteristic",
            propertyPath: "items",
            type: "groovy.SweQuantityBean"
        }
    }));
</script>

<div id="sweQuantityCharacteristic">
    <table class="verticalTop">
        <tr>
            <td>
            <span class="firstLabel width190">
                <span class="tooltip" id="sweQuantityCharacteristicLabel">
                    <fmt:message key="element.sweQuantityCharacteristic.label"/><br>
                </span>
                <input type="button" class="add" id="addsweQuantityCharacteristicButton"/>
            </span>
            </td>
            <td>
                <div style="clear:right" id="sweQuantityCharacteristicTabs">
                    <form:errors path="storage['sweQuantityCharacteristic'].items" cssClass="ui-state-error-text"/>
                    <ul>
                        <c:forEach items="${updateMetadata.storage['sweQuantityCharacteristic'].items}" varStatus="status">
                            <li>
                                <a href="#sweQuantityCharacteristic-${status.count}">
                                    <c:out value="${status.count}"/>
                                </a>
                                <script type="text/javascript">
                                    Spring.addDecoration(new SmartEditor.AjaxEventDecoration({
                                        elementId: "sweQuantityCharacteristicRemove_${status.count}",
                                        event: "onclick",
                                        formId: "updateMetadata",
                                        params: {fragments: "sweQuantityCharacteristic",
                                            method: "remove",
                                            elementName: "sweQuantityCharacteristic",
                                            propertyPath: "items",
                                            elementIndex: ${status.count-1} }
                                    }));
                                </script>
                            <span class="ui-icon ui-icon-close"
                                  id="sweQuantityCharacteristicRemove_${status.count}">RemoveTab</span>
                            </li>
                        </c:forEach>
                    </ul>
                    <c:forEach items="${updateMetadata.storage['sweQuantityCharacteristic'].items}" var="party"
                               varStatus="status">
                        <div id="sweQuantityCharacteristic-${status.count}">
                            <tiles:insertAttribute name="innerSweQuantityCharacteristic">
                                <tiles:putAttribute name="cnt" value="${status.count-1}"/>
                            </tiles:insertAttribute>
                        </div>
                    </c:forEach>
                </div>
            </td>
        </tr>
    </table>
</div>