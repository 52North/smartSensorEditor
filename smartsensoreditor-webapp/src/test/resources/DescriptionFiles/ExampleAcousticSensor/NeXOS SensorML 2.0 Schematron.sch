<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sml="http://www.opengis.net/sensorml/2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:swe="http://www.opengis.net/swe/2.0" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:xlink="http://www.w3.org/1999/xlink" xsi:schemaLocation="http://www.opengis.net/sensorml/2.0 http://schemas.opengis.net/sensorML/2.0/sensorML.xsd http://www.opengis.net/swe/2.0 http://schemas.opengis.net/sweCommon/2.0/swe.xsd" schemaVersion="ISO19757-3">
   <ns prefix="sml" uri="http://www.opengis.net/sensorml/2.0"/>
   <ns prefix="gml" uri="http://www.opengis.net/gml/3.2"/>
   <ns prefix="swe" uri="http://www.opengis.net/swe/2.0"/>
   <ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- General Validation  -->
   <!-- This pattern validates the parts of the SensorML document which are common for the System and the Component. -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <pattern id="GeneralValidation">
      <!--~~~~~~~~~~-->
      <!--gml:description   -->
      <!--~~~~~~~~~~-->
      <!-- 
            These rules ensure that for every system and component a gml:description is provided..
        -->
      <rule context="//sml:PhysicalSystem">
         <assert test="gml:description">Error: 'gml:description' element has to be present</assert>
      </rule>
      <rule context="//sml:PhysicalComponent">
         <assert test="gml:description">Error: 'gml:description' element has to be present</assert>
      </rule>
      <!--~~~~~~~~~~-->
      <!--Identification   -->
      <!--~~~~~~~~~~-->
      <!-- 
            Each "identifier/Term" element contained in the "IdentifierList" must have a "definition" attribute.
            This attribute links to the semantics of the identifier.
        -->
      <rule context="//sml:identification/sml:IdentifierList/sml:identifier/sml:Term">
         <assert test="string-length(@definition) > 0">Error: 'definition' attribute has to be present and its value has to be > 0.</assert>
      </rule>
      <!--
            One identifier has to contain the definition "http://www.nexosproject.eu/dictionary/definitions.html#UUID". 
            The value of its contained "Term" element uniquely identifies the instance.
        -->
      <rule context="//sml:identification">
         <assert test="count(sml:IdentifierList/sml:identifier/sml:Term[@definition = 'http://www.nexosproject.eu/dictionary/definitions.html#UUID']) = 1">Error: one identifier has to be of the type 'http://www.nexosproject.eu/dictionary/definitions.html#UUID'.</assert>
      </rule>
      <!--
            One identifier has to contain the definition "http://www.nexosproject.eu/dictionary/definitions.html#shortName".
            The value of  its contained "Term" element represents a human understandable name for the instance.
        -->
      <rule context="//sml:identification">
         <assert test="count(sml:IdentifierList/sml:identifier/sml:Term[@definition = 'http://www.nexosproject.eu/dictionary/definitions.html#shortName']) = 1">Error: one identifier has to be of the type 'http://www.nexosproject.eu/dictionary/definitions.html#shortName'.</assert>
      </rule>
      <!--
            One identifier has to contain the definition "http://www.nexosproject.eu/dictionary/definitions.html#shortName". 
            The value of its contained "Term" element represents a short representation of the human understandable name for the instance.
        -->
      <rule context="//sml:identification">
         <assert test="count(sml:IdentifierList/sml:identifier/sml:Term[@definition = 'http://www.nexosproject.eu/dictionary/definitions.html#shortName']) = 1">Error: one identifier has to be of the type 'http://www.nexosproject.eu/dictionary/definitions.html#shortName'.</assert>
      </rule>
   </pattern>
</schema>
