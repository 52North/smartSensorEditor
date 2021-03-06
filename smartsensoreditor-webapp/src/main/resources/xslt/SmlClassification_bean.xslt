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
	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="no" omit-xml-declaration="yes" />
	<xsl:template match="/">
		<multi_container>
			<xsl:for-each
				select="/*/sml:classification/sml:ClassifierList/sml:classifier">
				<SmlTerm>
					<definition>
						<xsl:value-of select="fn:normalize-space(./sml:Term/@definition)" />
					</definition>
					<label>
						<xsl:value-of select="fn:normalize-space(./sml:Term/sml:label)" />
					</label>
					<codeSpace>
						<xsl:value-of select="fn:normalize-space(./sml:Term/sml:codeSpace/@xlink:href)" />
					</codeSpace>
					<value>
						<xsl:value-of select="fn:normalize-space(./sml:Term/sml:value)" />
					</value>
				</SmlTerm>
			</xsl:for-each>
		</multi_container>
	</xsl:template>
</xsl:stylesheet>
