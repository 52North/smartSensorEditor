<?xml version="1.0" encoding="UTF-8"?>
<!-- See the NOTICE file distributed with this work for additional information 
	regarding copyright ownership. con terra GmbH licenses this file to You under 
	the Apache License, Version 2.0 (the "License"); you may not use this file 
	except in compliance with the License. You may obtain a copy of the License 
	at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable 
	law or agreed to in writing, software distributed under the License is distributed 
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
	express or implied. See the License for the specific language governing permissions 
	and limitations under the License. -->

<xsl:stylesheet version="2.0"
	xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd"
	xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:sml="http://www.opengis.net/sensorml/2.0"
	xmlns:swe="http://www.opengis.net/swe/2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xsi:schemaLocation="http://www.opengis.net/sensorml/2.0 http://schemas.opengis.net/sensorML/2.0/sensorML.xsd http://www.opengis.net/swe/2.0 http://schemas.opengis.net/sweCommon/2.0/swe.xsd"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="gmd gco gml sml fn swe">

	<!-- include base template -->
	<xsl:include href="/xslt/BaseTemplatesSML.xslt" />
	<!-- parameter handed over by transformer -->
	<xsl:param name="beanDoc" />

	<!-- go through citation and copy nodes -->
	<xsl:template match="/sml:PhysicalSystem/sml:classification"/>

	<xsl:template match="/sml:PhysicalSystem" >
		<xsl:copy>
			<xsl:attribute name="gml:id">
				<xsl:value-of select="@gml:id" />
				</xsl:attribute>
			<xsl:apply-templates select="gml:description" />
			<xsl:apply-templates select="gml:name" />
			<xsl:apply-templates select="gml:identifier" />
			<xsl:apply-templates select="sml:keywords" />
			<xsl:apply-templates select="sml:identification" />
					<xsl:if test="$beanDoc/*/SmlTerm">
			<sml:classification>
				<sml:ClassifierList>
					<xsl:for-each select="$beanDoc/*/SmlTerm">
						<sml:classifier>
							<sml:Term>
								<xsl:if test="fn:normalize-space(definition) != ''">
									<xsl:attribute name="definition">
							<xsl:value-of select="fn:normalize-space(definition)" />
						</xsl:attribute>
		
								</xsl:if>
								<sml:label>
						<xsl:value-of select="label" />
						</sml:label>
								<xsl:if test="fn:normalize-space(codeSpace) != ''">
									<sml:codeSpace>
										<xsl:attribute name="xlink:href">
							<xsl:value-of select="fn:normalize-space(codeSpace)" />
						</xsl:attribute>
									</sml:codeSpace>
								</xsl:if>
								<sml:value>
							<xsl:value-of select="fn:normalize-space(value)" />
						</sml:value>
							</sml:Term>
						</sml:classifier>
					</xsl:for-each>
				</sml:ClassifierList>
			</sml:classification>
		</xsl:if>
			<xsl:apply-templates select="sml:validTime" />
			<xsl:apply-templates select="sml:securityConstraints" />
			<xsl:apply-templates select="sml:legalConstraints" />
			<xsl:apply-templates select="sml:characteristics" />
			<xsl:apply-templates select="sml:capabilities" />
		    <xsl:apply-templates select="sml:contacts" />
		    <xsl:apply-templates select="sml:documentation" />
		    <xsl:apply-templates select="sml:history" />
		    <xsl:apply-templates select="sml:definition" />
		    <xsl:apply-templates select="sml:typeOf" />
		    <xsl:apply-templates select="sml:configuration" />
		    <xsl:apply-templates select="sml:featureOfInterest" />
		    <xsl:apply-templates select="sml:inputs" />
		    <xsl:apply-templates select="sml:outputs" />
		    <xsl:apply-templates select="sml:parameters" />
		    <xsl:apply-templates select="sml:modes" />
		    <xsl:apply-templates select="sml:attachedTo" />
		    <xsl:apply-templates select="sml:localReferenceFrame" />
		    <xsl:apply-templates select="sml:localTimeFrame" />
		    <xsl:apply-templates select="sml:position" />
		    <xsl:apply-templates select="sml:timePosition" />
		    <xsl:apply-templates select="sml:components" />
		    <xsl:apply-templates select="sml:connections" />
		</xsl:copy>

	</xsl:template>
	
</xsl:stylesheet>
