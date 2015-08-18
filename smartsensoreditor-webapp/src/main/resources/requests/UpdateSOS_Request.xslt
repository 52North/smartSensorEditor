<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ogc="http://www.opengis.net/ogc">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="no"/>
    <xsl:param name="sensorDescription"/>
    <xsl:template match="/">
  <swes:UpdateSensorDescription service="SOS" version="2.0.0"
            xmlns:swes="http://www.opengis.net/swes/2.0"
            xmlns:sos="http://www.opengis.net/sos/2.0"
            xmlns:swe="http://www.opengis.net/swe/2.0"
            xmlns:sml="http://www.opengis.net/sensorml/2.0"
            xmlns:gml="http://www.opengis.net/gml/3.2"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:gco="http://www.isotc211.org/2005/gco"
            xmlns:gmd="http://www.isotc211.org/2005/gmd" xsi:schemaLocation="http://www.opengis.net/swes/2.0 http://schemas.opengis.net/swes/2.0/swes.xsd">
            <swes:procedure>http://www.52north.org/test/procedure/9</swes:procedure>
            <swes:procedureDescriptionFormat>http://www.opengis.net/sensorml/2.0</swes:procedureDescriptionFormat>
            <swes:description>
                <swes:SensorDescription>
                    <swes:data>
                       <xsl:copy-of select="$sensorDescription"/>
                    </swes:data>
                </swes:SensorDescription>
            </swes:description>
        </swes:UpdateSensorDescription>
    </xsl:template>
</xsl:stylesheet>