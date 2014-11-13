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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<script type="text/javascript">
    $(document).ready(function () {
        // set up tabs
        var $nameTabs = $('#nameTabs').tabs();
        // select last tab that has been added
        $nameTabs.tabs('select', $nameTabs.tabs('length') - 1);
    });
    Spring.addDecoration(new SmartEditor.AjaxEventDecoration({
        elementId: "addNameButton",
        event: "onclick",
        formId: "updateMetadata",
        params: {fragments: "name",
            method: "add",
            elementName: "name",
            propertyPath: "items",
            type: "groovy.NameBean"
        }
    }));
</script>

<div id="name">
	<table class="verticalTop">
		<tr>
			<td><span class="firstLabel width190"> <span
					class="tooltip" id="nameLabel"> <fmt:message
							key="element.name.label" /><br>
				</span> <input type="button" class="add" id="addNameButton" />
			</span></td>
			<td>
				<div style="clear: right" id="nameTabs">
					<form:errors path="storage['name'].items"
						cssClass="ui-state-error-text" />
					<ul>
						<c:forEach items="${updateMetadata.storage['name'].items}"
							varStatus="status">
							<li><a href="#name-${status.count}"> <c:out
										value="${status.count}" />
							</a> <script type="text/javascript">
                                    Spring.addDecoration(new SmartEditor.AjaxEventDecoration({
                                        elementId: "nameRemove_${status.count}",
                                        event: "onclick",
                                        formId: "updateMetadata",
                                        params: {fragments: "name",
                                            method: "remove",
                                            elementName: "name",
                                            propertyPath: "items",
                                            elementIndex: ${status.count-1} }
                                    }));
                                </script> <span class="ui-icon ui-icon-close"
								id="nameRemove_${status.count}">RemoveTab</span></li>
						</c:forEach>
					</ul>
					<c:forEach items="${updateMetadata.storage['name'].items}"
						varStatus="status">
						<div id="name-${status.count}">
							<tiles:insertAttribute name="innerName">
								<tiles:putAttribute name="counter" value="${status.count-1}" />
							</tiles:insertAttribute>
						</div>
					</c:forEach>
				</div>
			</td>
		</tr>
	</table>
</div>
