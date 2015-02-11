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

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:sml="http://www.opengis.net/sensorML/1.0.1"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                exclude-result-prefixes="xs xsi gmd sml">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>
    <xsl:template match="/">
        <multi_container>
            <xsl:for-each select="//sml:member/*/sml:identification/sml:IdentifierList/sml:identifier">
                <SmlIdentification>
                    <name>
                        <xsl:value-of select="./@name"/>
                    </name>
                    <definition>
                        <xsl:value-of select="./sml:Term/@definition"/>
                    </definition>
                    <value>
                        <xsl:value-of select="./sml:Term/sml:value"/>
                    </value>
                </SmlIdentification>
            </xsl:for-each>
        </multi_container>
    </xsl:template>
</xsl:stylesheet>
