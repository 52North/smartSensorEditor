<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="no"/>
    <xsl:param name="soapHeaderMessage"/>
    <xsl:template match="/">
         <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
            <xsl:if test="$soapHeaderMessage">
                <soap:Header>
                    <xsl:copy-of select="$soapHeaderMessage"/>
                </soap:Header>
            </xsl:if>
            <soap:Body>
                <xsl:copy-of select="."/>
            </soap:Body>
        </soap:Envelope>
    </xsl:template>
</xsl:stylesheet>