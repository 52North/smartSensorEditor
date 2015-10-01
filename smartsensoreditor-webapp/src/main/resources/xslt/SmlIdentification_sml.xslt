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
	exclude-result-prefixes="gmd gco gml sml">

	<!-- include base template -->
	<xsl:include href="/xslt/BaseTemplatesSML.xslt" />
	<!-- parameter handed over by transformer -->
	<xsl:param name="beanDoc" />
	<!-- remove existing identifiers -->
	<xsl:template match="/*/sml:identification" />
	
		<xsl:template match="/sml:PhysicalSystem">
		<xsl:copy>
			<xsl:attribute name="gml:id">
				<xsl:value-of select="@gml:id" />
				</xsl:attribute>
			<xsl:apply-templates select="gml:identifier" />
			<xsl:apply-templates select="* except(sml:* | comment() | gml:identifier)" />
			<xsl:apply-templates select="sml:keywords" />
			<xsl:call-template name="identification" />
			<xsl:apply-templates select="sml:classification" />
			<xsl:apply-templates select="sml:validTime" />
			<xsl:apply-templates select="sml:securityConstraints" />
			<xsl:apply-templates select="sml:legalConstraints" />
			<xsl:apply-templates select="sml:characteristics" />
			<xsl:apply-templates select="sml:capabilities" />

			<xsl:apply-templates
				select="node() except(*[not(namespace-uri()='http://www.opengis.net/sensorml/2.0')]| sml:keywords | sml:identification | sml:classification | sml:validTime | sml:securityConstraint | sml:legalConstraints | sml:characteristics | sml:capabilities |  comment())" />
		</xsl:copy>

	</xsl:template>
	<!-- go through citation and copy nodes -->
	<xsl:template name="identification">
			<xsl:if test="$beanDoc/*/SmlTerm">
			<sml:identification>
				<sml:IdentifierList>
					<xsl:for-each select="$beanDoc/*/SmlTerm">
						<sml:identifier>
							<sml:Term>
								<xsl:attribute name="definition">
							<xsl:value-of select="fn:normalize-space(definition)" />
						</xsl:attribute>
								<sml:label>
						<xsl:value-of select="fn:normalize-space(label)" />
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
						</sml:identifier>
					</xsl:for-each>
				</sml:IdentifierList>
				</sml:identification>
			</xsl:if>

	</xsl:template>
</xsl:stylesheet>
