<?xml version="1.0" encoding="UTF-8"?>
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
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="gmd gco">

    <!-- include base template -->
    <xsl:include href="/xslt/BaseTemplates.xslt"/>
    <!-- parameter handed over by transformer -->
    <xsl:param name="beanDoc"/>
    
    <!-- remove existing alternateTitles -->
    <xsl:template match="gmd:identificationInfo/*/gmd:citation/*/gmd:alternateTitle"/>
    
    <!-- go through citation and copy nodes -->
    <xsl:template match="gmd:identificationInfo/*/gmd:citation/*">
        <xsl:copy>
            <xsl:apply-templates select="gmd:title"/>
               <xsl:apply-templates select="gmd:name"/> 
            <xsl:for-each select="$beanDoc/*/AlternateTitle">
                <gmd:alternateTitle>
                    <gco:CharacterString>
                        <xsl:value-of select="title"/>
                    </gco:CharacterString>
                </gmd:alternateTitle>
            </xsl:for-each>
            <xsl:apply-templates select="gmd:date"/>
            <xsl:apply-templates select="gmd:edition"/>
            <xsl:apply-templates select="gmd:editionDate"/>
            <xsl:apply-templates select="gmd:identifier"/>
            <xsl:apply-templates select="gmd:citedResponsibleParty"/>
            <xsl:apply-templates select="gmd:presentationForm"/>
            <xsl:apply-templates select="gmd:series"/>
            <xsl:apply-templates select="gmd:otherCitationDetails"/>
            <xsl:apply-templates select="gmd:collectiveTitle"/>
            <xsl:apply-templates select="gmd:ISBN"/>
            <xsl:apply-templates select="gmd:ISSN"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
