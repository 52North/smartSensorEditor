<!--

    See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    con terra GmbH licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License. You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:sml="http://www.opengis.net/sensorML/1.0.1"
	xmlns:gml="http://schemas.opengis.net/gml/3.1.1/base/gml.xsd" xmlns:swe="http://www.opengis.net/swe/1.0.1"
	exclude-result-prefixes=" gml sml swe">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>
    <xsl:template match="/">
        <SmlAcousticSensorLength>
            <length>
                <xsl:value-of select="//sml:member/sml:System/sml:characteristics/swe:DataRecord[@definition='urn:ogc:def:property:OGC:physicalProperties']/swe:field/swe:DataRecord/swe:field/swe:Quantity[@definition='urn:ogc:def:property:OGC:length']/swe:value"/>
            </length>
        </SmlAcousticSensorLength>
    </xsl:template>
</xsl:stylesheet>
