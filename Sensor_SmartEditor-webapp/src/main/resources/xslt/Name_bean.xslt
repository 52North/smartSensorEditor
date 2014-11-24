<?xml version="1.0" encoding="UTF-8"?>
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
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:sml="http://www.opengis.net/sensorML/1.0"
	xmlns:gml="http://schemas.opengis.net/gml/3.1.1/base/gml.xsd"
                exclude-result-prefixes=" gml sml">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>
     <xsl:template match="/">   
     <multi_container>
       <xsl:for-each select="//sml:member/*/sml:identification/sml:IdentifierList/sml:identifier[@name='longName']/sml:Term/sml:value">
                <Name>
                    <title>
                        <xsl:value-of select="./text()"/>
                    </title>
                </Name>  
                </xsl:for-each>
                </multi_container>       
    </xsl:template> 
</xsl:stylesheet>
