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

	<xsl:include href="/xslt/BaseTemplatesSML.xslt" />
	<!-- parameter handed over by transformer -->
	<xsl:param name="beanDoc" />

	<xsl:template
		match="/sml:PhysicalSystem/sml:capabilities/sml:CapabilityList/sml:capability/swe:Text" />
	<!-- go through citation and copy nodes -->
	<xsl:template match="/sml:PhysicalSystem" priority="200">
		<xsl:copy>
			<xsl:attribute name="gml:id">
				<xsl:value-of select="@gml:id" />
				</xsl:attribute>
			<xsl:apply-templates select="node()except(sml:*)" />
			<xsl:apply-templates select="sml:keywords" />
			<xsl:apply-templates select="sml:identification" />
			<xsl:apply-templates select="sml:classification" />
			<xsl:apply-templates select="sml:validTime" />
			<xsl:apply-templates select="sml:securityConstraints" />
			<xsl:apply-templates select="sml:legalConstraints" />
			<xsl:apply-templates select="sml:characteristics" />
			<xsl:apply-templates select="sml:capabilities" />
			<xsl:call-template name="insertCapabilities" />

			<xsl:apply-templates
				select="node() except(*[not(namespace-uri()='http://www.opengis.net/sensorml/2.0')]| sml:keywords | sml:identification | sml:classification | sml:validTime | sml:securityConstraint | sml:legalConstraints | sml:characteristics | sml:capabilities |  comment())" />
		</xsl:copy>

	</xsl:template>
	<xsl:template name="onlyTextNodes"
		match="/sml:PhysicalSystem/sml:capabilities[*/sml:capability/*[count(not(name(.) = 'swe:Text'))>0]]"
		priority="100" />

	<xsl:template name="otherChildrenThanText" match="/sml:PhysicalSystem/sml:capabilities"
		priority="50">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>

	</xsl:template>
	<xsl:template name="noChilds"
		match="/sml:PhysicalSystem/sml:capabilities[*/not(*)]" priority="200" />
	<xsl:template name="noChilds2"
		match="/sml:PhysicalSystem/sml:capabilities[not(*)]" priority="300" />


	<xsl:template name="insertCapabilities">
		<xsl:param name="count" select="1" />
		<xsl:param name="done">
		</xsl:param>
		<xsl:if test="number($count) &lt;= count($beanDoc/*/SmlCapabilityText)">
			<xsl:variable name="capability"
				select="$beanDoc/*/SmlCapabilityText[number($count)]" />
			<xsl:variable name="capabilitiesNameOld"
				select="fn:normalize-space($capability/capabilitiesName)" />
			<xsl:if
				test="(fn:not(/sml:PhysicalSystem/sml:capabilities/@name = $capabilitiesNameOld)) or 
				(/sml:PhysicalSystem/sml:capabilities[*/sml:capability/*[count(not(name(.) = 'swe:Text'))>0]]/@name = $capabilitiesNameOld) or
				(/sml:PhysicalSystem/sml:capabilities[*/not(*)]/@name = $capabilitiesNameOld) or 
				(/sml:PhysicalSystem/sml:capabilities[not(*)]/@name = $capabilitiesNameOld)">
				<xsl:if test="fn:not(tokenize($done,',') = $capabilitiesNameOld)">
					<sml:capabilities>
						<xsl:attribute name="name">
							<xsl:value-of select="$capabilitiesNameOld" />
						</xsl:attribute>
						<sml:CapabilityList>
							<xsl:for-each select="$beanDoc/*/SmlCapabilityText">
								<xsl:if test="$capabilitiesNameOld=capabilitiesName">
									<xsl:call-template name="beanValues">
										<xsl:with-param name="capability" select="." />
									</xsl:call-template>
								</xsl:if>

							</xsl:for-each>
						</sml:CapabilityList>
					</sml:capabilities>
				</xsl:if>
			</xsl:if>
			<xsl:call-template name="insertCapabilities">
				<xsl:with-param name="count" select="number($count) + 1" />
				<xsl:with-param name="done"
					select="fn:concat($done ,',', $beanDoc/*/SmlCapabilityText[number($count)]/capabilitiesName)" />
			</xsl:call-template>
		</xsl:if>

	</xsl:template>


	<xsl:template name="list"
		match="/sml:PhysicalSystem/sml:capabilities/sml:CapabilityList">
		<xsl:param name="capabilitiesNameNode" select="../@name" />
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
			<xsl:call-template name="insertCapabilitiesInOldNode">
				<xsl:with-param name="capabilitiesNameNode"
					select="fn:normalize-space($capabilitiesNameNode)" />
			</xsl:call-template>
		</xsl:copy>
	</xsl:template>

	<xsl:template name="insertCapabilitiesInOldNode">
		<xsl:param name="count" select="1" />
		<xsl:param name="capabilitiesNameNode" />
		<xsl:if test="number($count) &lt;= count($beanDoc/*/SmlCapabilityText)">
			<xsl:variable name="capability"
				select="$beanDoc/*/SmlCapabilityText[number($count)]" />
			<xsl:if test="$capabilitiesNameNode = $capability/capabilitiesName">
				<xsl:call-template name="beanValues">
					<xsl:with-param name="capability" select="$capability" />
				</xsl:call-template>
			</xsl:if>
			<xsl:call-template name="insertCapabilitiesInOldNode">
				<xsl:with-param name="count" select="number($count) + 1" />
				<xsl:with-param name="capabilitiesNameNode" select="$capabilitiesNameNode" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="beanValues">
		<xsl:param name="capability" />
		<sml:capability>
			<xsl:attribute name="name"> <xsl:value-of
				select="fn:normalize-space($capability/capabilityName)" /> </xsl:attribute>
			<swe:Text>
				<xsl:attribute name="definition"> <xsl:value-of
					select="fn:normalize-space($capability/definition)" /> </xsl:attribute>
				<xsl:if test="fn:normalize-space($capability/label) != ''">
					<swe:label>
						<xsl:value-of select="fn:normalize-space($capability/label)" />
					</swe:label>
				</xsl:if>
				<xsl:if
					test="(fn:normalize-space($capability/constraintValue[1]) != '') or (fn:normalize-space($capability/constraintPatterns) != '')">
					<swe:constraint>
						<swe:AllowedTokens>
							<xsl:if
								test="fn:normalize-space($capability/constraintValue[1]) != ''">
								<xsl:for-each select="fn:tokenize($capability/constraintValue,',')">
									<swe:value>
										<xsl:value-of select="fn:normalize-space(.)" />
									</swe:value>
								</xsl:for-each>
							</xsl:if>
							<xsl:if
								test="fn:normalize-space($capability/constraintPatterns) != ''">
								<swe:pattern>
									<xsl:value-of
										select="fn:normalize-space($capability/constraintPatterns)" />
								</swe:pattern>
							</xsl:if>
						</swe:AllowedTokens>
					</swe:constraint>
					</xsl:if>
					<xsl:if test="fn:normalize-space($capability/value) != ''">
						<swe:value>
							<xsl:value-of select="fn:normalize-space($capability/value)" />
						</swe:value>
					</xsl:if>
			</swe:Text>
		</sml:capability>
	</xsl:template>
</xsl:stylesheet>



								
