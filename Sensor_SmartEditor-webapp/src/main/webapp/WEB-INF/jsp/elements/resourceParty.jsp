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

<tiles:useAttribute name="cnt" id="resource_party_cnt"/>

<editor:fragments modelAttribute="updateMetadata">
    <table class="marginLine">
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}_IndividualName" class="firstLabel">
                    <fmt:message key="element.party.indiname"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].individualName" size="20"
                            id="resourceParty_${resource_party_cnt}_IndividualName"/>
            </td>
            <td>
                <label for="resourceParty_${resource_party_cnt}_OrganisationName" class="firstLabel">
                    <fmt:message key="element.party.organame"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].organisationName" size="20"
                            id="resourceParty_${resource_party_cnt}_OrganisationName"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}_PositionName" class="firstLabel">
                    <fmt:message key="element.party.positionname"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].positionName" size="20"
                            id="resourceParty_${resource_party_cnt}_PositionName"/>
            </td>
            <td>
                <label for="resourceParty_${resource_party_cnt}_Role" class="firstLabel">
                    <fmt:message key="element.general.role"/>
                </label></td><td>
                <form:select path="storage['resourceParty'].items[${resource_party_cnt}].role"
                             id="resourceParty_${resource_party_cnt}_Role" multiple="false">
                    <form:option value=""><fmt:message key="editor.general.choose"/></form:option>
                    <form:options items="${CI_RoleCode.nvp}" itemLabel="name" itemValue="value"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}_Telephone" class="firstLabel">
                    <fmt:message key="element.party.voice"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].voice" size="20"
                            id="resourceParty_${resource_party_cnt}_Voice"/>
            </td>
            <td>
                <label for="resourceParty_${resource_party_cnt}_Fax" class="firstLabel">
                    <fmt:message key="element.party.fax"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].fax" size="20"
                            id="resourceParty_${resource_party_cnt}_Fax"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}_DeliveryPoint" class="firstLabel">
                    <fmt:message key="element.party.deliverypoint"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].deliveryPoint" size="20"
                            id="resourceParty_${resource_party_cnt}_DeliveryPoint"/>
            </td>
            <td>
                <label for="resourceParty_${resource_party_cnt}_City" class="firstLabel">
                    <fmt:message key="element.party.city"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].city" size="20"
                            id="resourceParty_${resource_party_cnt}_City"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}_AdministrativeArea" class="firstLabel">
                    <fmt:message key="element.party.area"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].administrativeArea" size="20"
                            id="resourceParty_${resource_party_cnt}_AdministrativeArea"/>
            </td>
            <td>
                <label for="resourceParty_${resource_party_cnt}_PostalCode" class="firstLabel">
                    <fmt:message key="element.party.postalcode"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].postalCode" size="20"
                            id="resourceParty_${resource_party_cnt}_PostalCode"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}Country" class="firstLabel">
                    <fmt:message key="element.party.country"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].country" size="20"
                            id="resourceParty_${resource_party_cnt}_Country"/>
            </td>
            <td>
                <label for="resourceParty_${resource_party_cnt}_MailAddress" class="firstLabel">
                    <fmt:message key="element.party.mail"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].mailAddress" size="20"
                            id="resourceParty_${resource_party_cnt}_MailAddress"/>
            </td>
        </tr>
        <tr>
            <td>
                <label for="resourceParty_${resource_party_cnt}_Url" class="firstLabel">
                    <fmt:message key="element.party.url"/>
                </label></td><td>
                <form:input path="storage['resourceParty'].items[${resource_party_cnt}].url" size="20"
                            id="resourceParty_${resource_party_cnt}_Url"/>
            </td>
            <td>&nbsp;</td>
        </tr>
    </table>
</editor:fragments>