<xsl:stylesheet version="2.0"
	xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sml="http://www.opengis.net/sensorML/1.0.1"
	xmlns:gml="http://schemas.opengis.net/gml/3.1.1/base/gml.xsd"
	exclude-result-prefixes="gmd gco gml sml">
	<!-- import base template -->
	<xsl:include href="/xslt/BaseTemplatesSML.xslt" />
	
	<xsl:template
		match="//sml:member/*/sml:identification/sml:IdentifierList/sml:identifier/sml:Term[@definition='urn:ogc:def:identifier:OGC:1.0:uniqueID']" />
	<xsl:template
		match="//sml:member/*/sml:identification/sml:IdentifierList/sml:identifier[@name='uniqueID']" />
	<!-- parameter handed over by transformer -->
	<xsl:param name="beanDoc" />
	<xsl:template match="//sml:member/*/sml:identification/sml:IdentifierList">
		<xsl:copy>
			<xsl:apply-templates select="sml:identifier" />
			<sml:identifier name="uniqueID">
				<sml:Term definition="urn:ogc:def:identifier:OGC:1.0:uniqueID">
					<sml:value>
						<xsl:value-of select="$beanDoc/FileIdentifier/identifier" />
					</sml:value>
				</sml:Term>
			</sml:identifier>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
