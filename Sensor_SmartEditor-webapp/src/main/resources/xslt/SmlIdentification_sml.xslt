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
	xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:sml="http://www.opengis.net/sensorML/1.0.1"
	xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:srv="http://www.isotc211.org/2005/srv"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	exclude-result-prefixes="gmd gco srv sml">

	<!-- include base template -->
	<xsl:include href="/xslt/BaseTemplatesSML.xslt" />
	<!-- parameter handed over by transformer -->
	<xsl:param name="beanDoc" />
	<!-- remove existing identifiers -->
	<xsl:template match="//sml:member/*/sml:identification/sml:IdentifierList/*" />
	<!-- go through citation and copy nodes -->
	<xsl:template match="//sml:member/*/sml:identification/sml:IdentifierList">
		<xsl:copy>
			<xsl:for-each select="$beanDoc/*/SmlIdentification">
				<sml:identifier>
					<xsl:attribute name="name">
						<xsl:value-of select="name" />
					</xsl:attribute>
					<sml:Term>
						<xsl:attribute name="definition">
							<xsl:value-of select="definition" />
						</xsl:attribute>
						<sml:value>
							<xsl:value-of select="value" />
						</sml:value>
					</sml:Term>
				</sml:identifier>
			</xsl:for-each>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
