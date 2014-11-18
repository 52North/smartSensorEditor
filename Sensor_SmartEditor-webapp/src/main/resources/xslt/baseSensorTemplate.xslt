<!--

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

-->

<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                exclude-result-prefixes="gco gmd xsi">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>
    <!-- creates an insatnce of MD_Identifier -->
    <xsl:template name="Create_MD_Identifier">
        <xsl:param name="code"/>
        <gmd:MD_Identifier>
            <gmd:code>
                <gco:CharacterString>
                    <xsl:value-of select="$code"/>
                </gco:CharacterString>
            </gmd:code>
        </gmd:MD_Identifier>
    </xsl:template>
    <!-- creates an insatnce of RS_Identifier -->
    <xsl:template name="Create_RS_Identifier">
        <xsl:param name="code"/>
        <xsl:param name="codeSpace"/>
        <xsl:param name="version"/>
        <gmd:RS_Identifier>
            <gmd:code>
                <gco:CharacterString>
                    <xsl:value-of select="$code"/>
                </gco:CharacterString>
            </gmd:code>
            <gmd:codeSpace>
                <gco:CharacterString>
                    <xsl:value-of select="$codeSpace"/>
                </gco:CharacterString>
            </gmd:codeSpace>
            <gmd:version>
                <gco:CharacterString>
                    <xsl:value-of select="$version"/>
                </gco:CharacterString>
            </gmd:version>
        </gmd:RS_Identifier>
    </xsl:template>
    <!-- creates a codelist -->
    <xsl:template name="CreateCodeList">
        <xsl:param name="codeListName"/>
        <xsl:param name="codeListNamespace"/>
        <xsl:param name="codeListValue"/>
        <xsl:param name="codeList"/>
        <xsl:param name="codeSpace"/>
        <xsl:param name="domainCode"/>
        <xsl:element name="{$codeListName}" namespace="{$codeListNamespace}">
            <xsl:attribute name="codeListValue">
                <xsl:value-of select="$codeListValue"/>
            </xsl:attribute>
            <xsl:choose>
                <xsl:when test="$codeList != ''">
                    <xsl:attribute name="codeList">
                        <xsl:value-of select="$codeList"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="codeList">
                        <xsl:text>http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#</xsl:text>
                        <xsl:value-of select="$codeListName"/>
                    </xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="$codeSpace != ''">
                <xsl:attribute name="codeSpace">
                    <xsl:value-of select="$codeSpace"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="$domainCode != ''">
                <xsl:value-of select="$domainCode"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <!-- creates a respoonsible party instance -->
    <xsl:template name="CreateResponsibleParty">
        <xsl:param name="organisationName"/>
        <xsl:param name="positionName"/>
        <xsl:param name="individualName"/>
        <xsl:param name="role"/>
        <xsl:param name="voice"/>
        <xsl:param name="fax"/>
        <xsl:param name="deliveryPoint"/>
        <xsl:param name="city"/>
        <xsl:param name="administrativeArea"/>
        <xsl:param name="postalCode"/>
        <xsl:param name="country"/>
        <xsl:param name="mailAddress"/>
        <xsl:param name="url"/>
        <xsl:element name="gmd:CI_ResponsibleParty">
            <xsl:if test="$individualName != ''">
                <xsl:element name="gmd:individualName">
                    <xsl:element name="gco:CharacterString">
                        <xsl:value-of select="$individualName"/>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
            <xsl:if test="$organisationName != ''">
                <xsl:element name="gmd:organisationName">
                    <xsl:element name="gco:CharacterString">
                        <xsl:value-of select="$organisationName"/>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
            <xsl:if test="$positionName != ''">
                <xsl:element name="gmd:positionName">
                    <xsl:element name="gco:CharacterString">
                        <xsl:value-of select="$positionName"/>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
            <xsl:if test="$voice != '' or $fax != '' or $deliveryPoint != '' or $city != '' or $administrativeArea != '' or $postalCode != '' or $country != '' or $mailAddress != '' or $url != ''">
                <xsl:element name="gmd:contactInfo">
                    <xsl:element name="gmd:CI_Contact">
                        <xsl:if test="$voice != '' or $fax != ''">
                            <xsl:element name="gmd:phone">
                                <xsl:element name="gmd:CI_Telephone">
                                    <xsl:if test="$voice != ''">
                                        <xsl:element name="gmd:voice">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$voice"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="$fax != ''">
                                        <xsl:element name="gmd:facsimile">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$fax"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                </xsl:element>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$deliveryPoint != '' or $city != '' or $administrativeArea != '' or $postalCode != '' or $country != '' or $mailAddress != ''">
                            <xsl:element name="gmd:address">
                                <xsl:element name="gmd:CI_Address">
                                    <xsl:if test="$deliveryPoint != ''">
                                        <xsl:element name="gmd:deliveryPoint">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$deliveryPoint"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="$city != ''">
                                        <xsl:element name="gmd:city">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$city"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="$administrativeArea != ''">
                                        <xsl:element name="gmd:administrativeArea">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$administrativeArea"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="$postalCode != ''">
                                        <xsl:element name="gmd:postalCode">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$postalCode"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="$country != ''">
                                        <xsl:element name="gmd:country">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$country"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                    <xsl:if test="$mailAddress != ''">
                                        <xsl:element name="gmd:electronicMailAddress">
                                            <xsl:element name="gco:CharacterString">
                                                <xsl:value-of select="$mailAddress"/>
                                            </xsl:element>
                                        </xsl:element>
                                    </xsl:if>
                                </xsl:element>
                            </xsl:element>
                        </xsl:if>
                        <xsl:if test="$url != ''">
                            <xsl:element name="gmd:onlineResource">
                                <xsl:element name="gmd:CI_OnlineResource">
                                    <xsl:element name="gmd:linkage">
                                        <xsl:element name="gmd:URL">
                                            <xsl:value-of select="$url"/>
                                        </xsl:element>
                                    </xsl:element>
                                </xsl:element>
                            </xsl:element>
                        </xsl:if>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
            <xsl:if test="$role != ''">
                <xsl:element name="gmd:role">
                    <xsl:element name="gmd:CI_RoleCode">
                        <xsl:attribute name="codeListValue">
                            <xsl:value-of select="$role"/>
                        </xsl:attribute>
                        <xsl:attribute name="codeList">
                            <xsl:text>http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_RoleCode</xsl:text>
                        </xsl:attribute>
                        <xsl:value-of select="$role"/>
                    </xsl:element>
                </xsl:element>
            </xsl:if>
        </xsl:element>
    </xsl:template>
    <!-- get rid of PT_FreeText -->
    <xsl:template match="@xsi:type[ends-with(., 'PT_FreeText_PropertyType')]"/>
    <xsl:template match="gmd:PT_FreeText"/>
    <!-- normalize spaces -->
    <xsl:strip-space elements="*"/>
    <!-- copy everything else -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>