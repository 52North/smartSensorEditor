<!-- Copyright (C) 2014-2015 52°North Initiative for Geospatial Open Source 
	Software GmbH This program is free software; you can redistribute it and/or 
	modify it under the terms of the GNU General Public License version 2 as 
	published by the Free Software Foundation. If the program is linked with 
	libraries which are licensed under one of the following licenses, the combination 
	of the program with the linked library is not considered a "derivative work" 
	of the program: - Apache License, version 2.0 - Apache Software License, 
	version 1.0 - GNU Lesser General Public License, version 3 - Mozilla Public 
	License, versions 1.0, 1.1 and 2.0 - Common Development and Distribution 
	License (CDDL), version 1.0 Therefore the distribution of the program linked 
	with libraries licensed under the aforementioned licenses, is permitted by 
	the copyright holders if the distribution is compliant with both the GNU 
	General Public License version 2 and the aforementioned licenses. This program 
	is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
	PURPOSE. See the GNU General Public License for more details. -->

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ogc="http://www.opengis.net/ogc">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"
		omit-xml-declaration="no" />
	<!-- parameter handed over by RequestFactory -->
	<xsl:param name="procedureId" />
	<xsl:template match="/">
		<swes:DescribeSensor service="SOS" version="2.0.0"
			xmlns:swes="http://www.opengis.net/swes/2.0" xmlns:gml="http://www.opengis.net/gml/3.2"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.opengis.net/swes/2.0 http://schemas.opengis.net/swes/2.0/swes.xsd">
			<swes:procedure>
				<xsl:value-of select="$procedureId" />
			</swes:procedure>
			<swes:procedureDescriptionFormat>http://www.opengis.net/sensorml/2.0</swes:procedureDescriptionFormat>
		</swes:DescribeSensor>
	</xsl:template>
</xsl:stylesheet>