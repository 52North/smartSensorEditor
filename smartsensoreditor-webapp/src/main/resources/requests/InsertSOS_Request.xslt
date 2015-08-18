<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ogc="http://www.opengis.net/ogc">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="no"/>
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
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_1</swes:observableProperty>
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_2</swes:observableProperty>
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_3</swes:observableProperty>
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_4</swes:observableProperty>
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_5</swes:observableProperty>
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_6</swes:observableProperty>
            <swes:observableProperty>http://www.52north.org/test/observableProperty/9_7</swes:observableProperty>
            <swes:metadata>
                <sos:SosInsertionMetadata>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement</sos:observationType>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation</sos:observationType>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation</sos:observationType>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation</sos:observationType>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation</sos:observationType>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_GeometryObservation</sos:observationType>
                    <sos:observationType>http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_SWEArrayObservation</sos:observationType>
                    <!-- multiple values possible -->
                    <sos:featureOfInterestType>http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint</sos:featureOfInterestType>
                </sos:SosInsertionMetadata>
            </swes:metadata>
        </swes:InsertSensor>
    </xsl:template>
</xsl:stylesheet>