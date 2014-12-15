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
	xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sml="http://www.opengis.net/sensorML/1.0.1"
	xmlns:gml="http://schemas.opengis.net/gml/3.1.1/base/gml.xsd"
	exclude-result-prefixes="gmd gco gml sml">
	<xsl:include href="/xslt/BaseTemplatesSML.xslt" />
	<!-- parameter handed over by transformer -->
	<xsl:param name="beanDoc" />
	<!-- remove existing Names -->
	<xsl:template match="//sml:member/*/sml:keywords" />

	<!-- go through citation and copy nodes -->
	<xsl:template match="//sml:member/*">
		<xsl:copy>
			<xsl:apply-templates select="gmd:description" />
			<sml:keywords>
				<sml:KeywordList>
					<xsl:for-each select="$beanDoc/*/SmlKeyword">
						<sml:keyword>
							<xsl:value-of select="keyword" />
						</sml:keyword>
					</xsl:for-each>
				</sml:KeywordList>
			</sml:keywords>
			<xsl:apply-templates select="sml:identification" />
			<xsl:apply-templates select="sml:classification" />
			<xsl:apply-templates select="sml:validTime" />
			<xsl:apply-templates select="sml:capabilities" />
			<xsl:apply-templates select="sml:contact" />
			<xsl:apply-templates select="sml:position" />
			<xsl:apply-templates select="sml:interfaces" />
			<xsl:apply-templates select="sml:inputs" />
			<xsl:apply-templates select="sml:outputs" />
			<xsl:apply-templates select="sml:components" />
		</xsl:copy>

	</xsl:template>

</xsl:stylesheet>
