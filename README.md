Sensor_SmartEditor
==================
The Sensor\_SmartEditor is an extension on the smartEditor <sup>1</sup>. The smartEditor provides a form to easily edit ISO 19139 v1.0 and INSPIRE metadata documents. With the smartEditor templates can be created and the edited data are validated against the standard (error highlighting). Also the data can be uploaded to an OGC CSW 2.02 endpoint to communicate with a metadata management system.

Next to the functionalities of the smartEditor the Sensor\_SmartEditor provides the possibility to edit metadata based on SensorML v. 1.0.1.

<sup>1</sup>: http://52north.org/communities/metadata-management/smarteditor   smartEditor

1. Version Notes
--------------
Java: jdk 1.7.0_71 <br>
Apache Maven: 3.2.3 <br>
Apache Tomcat: 7.0.56 <br>
smartEditor: 2.1.6 <br>

2. License
----------------

3. Run Sensor_SmartEditor
-------------
To run the software the project has to be downloaded using git. You can do it using the console, choose a folder for the download via the command cd. 
The download can be done using HTTPS:
<pre><code>git clone https://github.com/janaGit/Sensor_SmartEditor.git
</code></pre>

Next you have to make sure that the needed software (Java, Maven and Tomcat) are installed. Then the code can be compiled and built using maven. You have to perform the following instruction within the main folder of the Sensor\_SmartEditor project. 
<pre><code>mvn clean package
</code></pre>
||  So both modules, the Sensor\_SmartEditor-api and the Sensor\_SmartEditor-webapp are build. Within the section "How two" it is explained how only one of the submodules can be built with maven.  ||

After that the created file "Sensor\_SmartEditor.war" which is located in the folder "Sensor\_SmartEditor\Sensor_SmartEditor-webapp\target" has to be put into the webapp folder of the tomcat project.
Then you can start Tomcat, open the browser and go to: http://localhost:8080/Sensor_SmartEditor/

The form for SensorML can be opened by clicking on the button "sensor".


4. Structure
---------
The Sensor\_SmartEditor uses maven to get the code of the smartEditor. The Sensor\_SmartEditor uses two components of the smartEditor: The smartEditor-api and the smartEditor-webapp. New files and files from the smartEditor which were needed to be modified are within the folders of this project.

To mix the files of the smartEditor project with the one of this project two different ways are used. For the smartEditor-webapp the maven-war-plugin with its overlay functionality is used. Because the smartEditor-api is a war-file the overlay could not be used. Instead the maven-shade-plugin mixes the .class files.

The smartEditor uses the Spring Framework. For functionalities which should be easily adapted and extended the smartEditor uses XML and XSLT files. So the most added files are XML, XSLT, .properties and .groovy files. For the front end java server pages (jsp) are used. The back end runs on java. To test the implemented code several JUnit tests were written. 

The following files of the smartEditor-api sub project are needed to be modified:

- SaveLocalController.java within the package de.conterra.smarteditor.controller 
 " lFileId=lUtil.evaluateAsString("//sml:member/sml:System/sml:identification/sml:IdentifierList/sml:identifier/sml:Te  rm[@definition='urn:ogc:def:identifier:OGC:1.0:uniqueID']/sml:value/text()", lDoc); "
Here the specific identifier has to be discovered to name the file which should be stored locally.

- BackendManagerService.java within the package de.conterra.smarteditor.service
  - method "public String getResourceType()" adapted to get to know if the resource type is== sensor.
  - new method "public boolean isBeanActive(String beanName)" created to get sure that only the needed beans for the  
    actual form are merged with the document.
  - method "public String getFileIdentifier()" adapted to get the Id from the BaseBean "smlUniqueID" when the sensorML     form is used.
  - method "public void newMetadataIdentifier()" to set the new ID for the smlUniqueID bean.
  - Property "activeBeanNamesRegex" to get the regex which is used in the method "public boolean  
    isBeanActive(String beanName)"

- EditorContext.java within the package de.conterra.smarteditor.xml
  - method "public String getNamespaceURI(String prefix)" and method public String getPrefix(String namespace) for sml     namespace adopted

The following files of the smartEditor-webapp sub project are needed to be modified or created:

 - create new groovy beans in resources/groovy
 - create new file SimpleCopy.xslt in resources/internal
 - create new template sensor.xml in resources/templates
 - create new XML file for validation in resources/validation
 - create new XSLT files to transform between bean and document in resources/xslt
 - adapt codelist_enumeration.xml for a new sensor button in resources/
 - adapt isolist_de.properties for a new sensor button in resources/
 - adapt isolist.properties for a new sensor button in resources/
 - adapt messages_de.properties for error messages, element labels and text about validation in resources/
 - adapt messages.properties for error messages, element labels and text about validation in resources/
 - create new javascript file for highlighting the form fields in webapp/js/validation
 - create build.properties to declare the database connection in webapp/META-INF
 - create file log4j.xml to define the logging variables in webapp/WEB-INF/classes
 - adapt tiles-editor.xml to declare the .jsp files for each form element and body_sensor.jsp in webapp/WEB-INF/defs
 - adapt tiles-elements.xml to define the .jsp files for multi form elements in webapp/WEB-INF/defs
 - create new jsp-files for the different form elements in webapp/WEB-INF/jsp/elements
 - (adapt start.jsp for the title Sensor_SmartEditor in webapp/WEB-INF/jsp/start)
 - create new body_sensor.jsp to define which elements should contain the form in webapp/WEB-INF/jsp
 - adapt menu.jsp to show the right error message (title or UniqueID is missing in method 
   "uniqueID\_or\_Title_missing()") in webapp/WEB-INF/jsp
 - adapt beans-definitions.xml to define the beans of the form elements and declare them in backendBean in 
   webapp/WEB-INF
 - adapt service-definitions.xml to declare the validator and set the activeBeanNamesRegex in webapp/WEB-INF
 - adapt validator-definitions.xml to define the validator in webapp/WEB-INF

5. How To
-------
###How to build the sub modules of the project with maven ?###
If only one of the sub modules should be build with maven, then go to the main folder of the whole project. With the attribute -pl a specific modul can be selected, like:
<pre><code>mvn clean package -pl Sensor_SmartEditor-webapp
</code></pre>
With the following command also all sub projects on which the sub project Sensor\_SmartEditor-webapp depends are also built: 
<pre><code>mvn clean package -pl Sensor_SmartEditor-webapp -am
</code></pre>
In this case the whole project is build and this command is the same like: mvn clean install.
(see: http://blog.sonatype.com/2009/10/maven-tips-and-tricks-advanced-reactor-options)

###How to create my own form ?###
In the future there will be a website which explains which different files have to modified and created to build new forms and form elements and make it possible to validate them against an arbitrary standard.


6. Known Bugs AND To-Dos
-----------
###fehler, wenn unique Id gelöscht wurde###
###newMetadataIdentifier Methode überarbeiten###
### ISO Formular funktioniert nicht###
