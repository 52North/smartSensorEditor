/**
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * con terra GmbH licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package groovy;


import com.thoughtworks.xstream.annotations.XStreamAlias
import de.conterra.smarteditor.beans.BaseBean
/**
<element name="Text" substitutionGroup="swe:AbstractSimpleComponent" type="swe:TextType"/> 
 
<complexType name="TextType"> 
  <complexContent> 
    <extension base="swe:AbstractSimpleComponentType"> 
      <sequence> 
        <element name="constraint" maxOccurs="1" minOccurs="0"                                                             IMPLEMENTED (see down)
               type="swe:AllowedTokensPropertyType"/> 
        <element name="value" maxOccurs="1" minOccurs="0" type="string"/>                                                  IMPLEMENTED
      </sequence> 
    </extension> 
  </complexContent> 
</complexType> 
(Reference:
<complexType name="AllowedTokensType"> 
  <complexContent> 
    <extension base="swe:AbstractSWEType"> 
      <sequence> 
        <element name="value" type="string" minOccurs="0" maxOccurs="unbounded"/>                                          IMPLEMENTED
        <element name="pattern" type="string" minOccurs="0"/>                                                              IMPLEMENTED
      </sequence> 
    </extension> 
  </complexContent> 
</complexType> )

Extension of:
<complexType name="AbstractSimpleComponentType" abstract="true"> 
  <complexContent> 
    <extension base="swe:AbstractDataComponentType"> 
      <sequence> 
        <element name="quality" type="swe:QualityPropertyType" minOccurs="0" maxOccurs="unbounded"/> 
        <element name="nilValues" type="swe:NilValuesPropertyType" minOccurs="0"/> 
      </sequence> 
      <attribute name="referenceFrame" type="anyURI" use="optional"/> 
      <attribute name="axisID" type="string" use="optional"/> 
    </extension> 
  </complexContent> 
</complexType> 
Extension of:
<complexType name="AbstractDataComponentType" abstract="true"> 
  <complexContent> 
    <extension base="swe:AbstractSWEIdentifiableType"> 
      <attribute name="definition" type="anyURI" use="required"/>                                                         IMPLEMENTED
      <attribute name="updatable" type="boolean" use="optional"/> 
      <attribute name="optional" type="boolean" use="optional" default="false"/> 
    </extension> 
  </complexContent> 
</complexType> 

Extension of:
<complexType name="AbstractSWEIdentifiableType"> 
  <complexContent> 
    <extension base="swe:AbstractSWEType"> 
      <sequence> 
        <element name="identifier" type="anyURI" minOccurs="0"/> 
        <element name="label" type="string" minOccurs="0"/>                                                                IMPLEMENTED 
        <element name="description" type="string" minOccurs="0"/> 
      </sequence> 
    </extension> 
Extension of:
<element name="AbstractSWE" abstract="true" type="swe:AbstractSWEType"/> 
 
<complexType name="AbstractSWEType"> 
  <sequence> 
    <element name="extension" type="anyType" minOccurs="0" maxOccurs="unbounded"/> 
  </sequence> 
  <attribute name="id" use="optional"/> 
</complexType> 
*/
@XStreamAlias("SmlCapabilityText")
class SmlCapabilityText extends BaseBean {
String capabilitiesName
String capabilityName
String definition
String label
String constraintValue
String constraintPatterns
String value				
}
