<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ogc="http://www.opengis.net/ogc">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="no"/>
    <xsl:param name="swesObservablePropertyList" />
    <xsl:param name="sosObservationTypeList"/>
    <xsl:param name="sosFeatureOfInterestTypeList"/>
    <xsl:template match="/">
        <swes:InsertSensor service="SOS" version="2.0.0"
            xmlns:swes="http://www.opengis.net/swes/2.0"
            xmlns:sos="http://www.opengis.net/sos/2.0"
            xmlns:swe="http://www.opengis.net/swe/2.0"
            xmlns:sml="http://www.opengis.net/sensorml/2.0"
            xmlns:gml="http://www.opengis.net/gml/3.2"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:gco="http://www.isotc211.org/2005/gco"
            xmlns:gmd="http://www.isotc211.org/2005/gmd">
            <swes:procedureDescriptionFormat>http://www.opengis.net/sensorml/2.0</swes:procedureDescriptionFormat>
            <swes:procedureDescription>
               <xsl:copy-of select="."/>
            </swes:procedureDescription>
            <!-- multiple values possible -->
            <xsl:for-each select="$swesObservablePropertyList">
				 <swes:observableProperty>
				 		<xsl:value-of select="." />
				 </swes:observableProperty>
			</xsl:for-each>
            <swes:metadata>
                <sos:SosInsertionMetadata>
                  <xsl:for-each select="$sosObservationTypeList">
				 <sos:observationType>
				 		<xsl:value-of select="." />
				 </sos:observationType>
			     </xsl:for-each>
			        <xsl:for-each select="$sosFeatureOfInterestTypeList">
				 <sos:featureOfInterestType>
				 		<xsl:value-of select="." />
				 </sos:featureOfInterestType>
			     </xsl:for-each>
                </sos:SosInsertionMetadata>
            </swes:metadata>
        </swes:InsertSensor>
    </xsl:template>
</xsl:stylesheet>