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
	<xsl:template
		match="/*/sml:capabilities/sml:CapabilityList/sml:capability/swe:Text" />
	<!-- go through citation and copy nodes -->
	<xsl:template match="/*/sml:capabilities">
		<xsl:copy>
			<xsl:if test="$beanDoc/*/SmlCapabilityText">
				<sml:CapabilityList>
					<xsl:for-each select="$beanDoc/*/SmlCapabilityText">
						<sml:capability>
							<xsl:attribute name="name">
							<xsl:value-of select="fn:normalize-space(capabilityName)" />
						</xsl:attribute>
							<swe:Text>
								<xsl:attribute name="definition">
							<xsl:value-of select="fn:normalize-space(definition)" />
						</xsl:attribute>
								<swe:label>
									<xsl:value-of select="fn:normalize-space(label)" />
								</swe:label>
								<swe:constraint>
									<swe:AllowedTokens>
								<xsl:for-each select="fn:tokenize(constraintValue,',')">
									<swe:value>
								<xsl:value-of select="fn:normalize-space(.)" />
								</swe:value>  
								</xsl:for-each> 
										<swe:pattern>
									<xsl:value-of select="fn:normalize-space(constraintPatterns)" />
								</swe:pattern>
									</swe:AllowedTokens>
								</swe:constraint>
								<swe:value>
									<xsl:value-of select="fn:normalize-space(value)" />
								</swe:value>
							</swe:Text>
						</sml:capability>
					</xsl:for-each>

				</sml:CapabilityList>
			</xsl:if>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
