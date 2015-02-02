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

2. Installation
-------------
To run the software the project has to be downloaded using git. You can do it using the console, choose a folder for the download via the command cd. 
The download can be done using HTTPS:
<pre><code>git clone https://github.com/janaGit/Sensor_SmartEditor.git
</code></pre>

Next you have to make sure that the needed software (Java, Maven and Tomcat) are installed. Then the code can be compiled and built using maven:
<pre><code>mvn clean package
</code></pre>

After that the created file "Sensor\_SmartEditor.war" which is located in the folder "Sensor\_SmartEditor\Sensor_SmartEditor-webapp\target" has to be put into the webapp folder of the tomcat project.
Then you can start Tomcat, open the browser and go to: http://localhost:8080/Sensor_SmartEditor/

The form for SensorML can be opened by clicking on the button "sensor".


3. Structure
---------
The Sensor\_SmartEditor uses maven to get the code of the smartEditor. The Sensor\_SmartEditor uses two components of the smartEditor: The smartEditor-api and the smartEditor-webapp. New files and files from the smartEditor which were needed to be modified are within the folders of this project.

To mix the files of the smartEditor project with the one of this project two different ways are used. For the smartEditor-webapp the maven-war-plugin with its overlay functionality is used. Because the smartEditor-api is a war-file the overlay could not be used. Instead the maven-shade-plugin mixes the .class files.

The smartEditor uses the Spring Framework. For functionalities which should be easily adapted and extended the smartEditor uses XML and XSLT files. So the most added files are XML, XSLT, .properties and .groovy files. For the front end java server pages (jsp) are used. The back end runs on java. To test the implemented code several JUnit tests were written. 

4. How To
-------
###submodule bauen dokumentieren:###

5. Known Bugs AND To-Dos
-----------
###fehler, wenn unique Id gelöscht wurde###
###newMetadataIdentifier Methode überarbeiten###
### ISO Formular funktioniert nicht###
