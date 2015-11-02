# smartSensorEditor

smartSensorEditor is an extension on the [smartEditor](http://52north.org/communities/metadata-management/smarteditor). The smartEditor provides a form-based user interface to easily edit ISO 19139 and INSPIRE metadata documents, with support for templates and validation, including error highlighting. Also the data can be uploaded to an OGC CSW 2.02 endpoint to communicate with a metadata management system.

smartSensorEditor extends these functionalities to to edit metadata based on SensorML v. 2.0. Although the original smartEditor was not designed to handle any other metadata standards, it was suitable to be extended to reuse aspects such as server-side validation with Schematron, user interface and styling, and drafts and template management.


## Version Notes

This project was tested with the following versions:

* Java JDK: `1.7.x`
* Apache Maven: `3.2.3`
* Apache Tomcat: `7.0.x`
* smartEditor: `2.1.6`


## Prepare Postgres Database for smartEditor

See https://wiki.52north.org/bin/view/Metadata/SmartEditorInstallationGuide#Database_preparation


## Install Sensor Editor

You must download and build the project from source using [git](http://git-scm.com/) and [Maven](http://maven.apache.org/):

Clone source code repository: 

```
git clone https://github.com/52North/smartSensorEditor.git
```

To build api and webapp modules, go to the project directory (by default ``smartSensorEditor``) and invoke the Maven build command:
Because of the maven notice plugin the following command needs an internet connection to check the license of the third party software.
```
cd smartSensorEditor
mvn clean install
```
The created war file ``sensorSmartEditor.war`` can then be found in: "SmartSensorEditor/smartsensoreditor-webapp/target".

Configure Tomcat: </br>
Next you have to make sure that the needed runtime environment (Java and Tomcat) are properly installed.
To make sure that the right java version is used (for linux, windoes is similar):</br>
Create a file setenv.sh (for linux) and write the following: </br>
JAVA_HOME=<Path to Java>/jdk1.7.YYYY </br>
JRE_HOME=<Path to Java>/jdk1.7.YYYY/jre </br>
If another program should run on the tomcat then the an additional line in setenv.sh has to be added: </br>
export JAVA_OPTS="-Xms128m -Xmx1024m -XX:MaxPermSize=256m -server" </br>

Quick start: </br>
Put the file ``sensorSmartEditor.war`` into the webapp folder of Tomcat and start Tomcat. 

Start with all features: </br>
If the feature for storing templates should be available, then additionally a database has to be created. For more information please take a look at the following link:</br> https://wiki.52north.org/bin/view/Metadata/SmartEditorInstallationGuide#Database_preparation </br>
The database URL, username, the password and the database driver have to be accessible for Tomcat. The instructions can be found at this link: </br>
https://wiki.52north.org/bin/view/Metadata/SmartEditorInstallationGuide#Servlet_container_configuration


Then open the browser at http://localhost:8080/smartSensorEditor.
The form for SensorML can be opened by clicking on the button "Sensor".


## Documentation

User and developer documentation for smartEditor can be found [in the 52North wiki](https://wiki.52north.org/bin/view/Metadata/SmartEditor).

Documentation specific to smartSensorEditor is in this document.


## Support

You can [report bugs on GitHub](https://github.com/52North/smartSensorEditor/issues) and ask for help on the [Metadata Management community mailing list](http://metadata.forum.52north.org/) (after looking at the [guidelines](http://52north.org/resources/mailing-list-and-forums/mailinglist-guidelines)).


## Develop

### Contributors

* [@janaGit](https://github.com/janaGit)
* [@nuest](https://github.com/nuest)

To contribute to this project you must sign the [52°North CLA](http://52north.org/about/licensing/).


### Collaboration Model

We follow the fork & pull development model: https://help.github.com/articles/using-pull-requests

### Code Structure

smartSensorEditor is a Maven overlay of the original smartEditor webapp. It consists of two modules:

* ``smartSensorEditor-api``: Java classes
* ``smartSensorEditor-webapp``: the web application, building on smartEditor-webapp. The [maven-war-plugin](http://maven.apache.org/plugins/maven-war-plugin/) with its [overlay functionality](http://maven.apache.org/plugins/maven-war-plugin/overlays.html) is used to replace and add new files.

smartEditor uses the [Spring Framework](http://projects.spring.io/spring-framework/) and all configuration happens via Spring Beans in XML files. For processing of XML documents, smartEditor uses a combination of XSLT and Groovy. For the front end Java Server Pages (jsp) are used. Therefore files of all of the above types were added to adjust smartEditor for SensorML and unit tests were written to ensure working code.


#### Changes to smartEditor

The following files of the smartEditor-api sub project are modified:

* org.n52.smartsensoreditor.beans
  * BackendBeanSML.java </br>
    original file: de.conterra.smarteditor.beans.BackendBean.java
  * PublishBeanSML.java </br>
    original file: de.conterra.smarteditor.beans.PublishBean.java
  * StartEditorBeanSML.java </br>
    original file: de.conterra.smarteditor.beans.StartEditorBean.java
* org.n52.smartsensoreditor.controller
  * BasicPublishControllerSML.java </br>
    original file: de.conterra.smarteditor.controller.BasicPublishController.java
  * EditControllerSML.java </br>
    original file: de.conterra.smarteditor.controller.EditController.java
  * SaveLocalControllerSML.java </br>
    original file: de.conterra.smarteditor.controller.SaveLocalController.java
  * SaveTemplateControllerSML.java </br>
    original file: de.conterra.smarteditor.controller.SaveTemplateController.java
  * StartEditorControllerSML.java </br>
    original file: de.conterra.smarteditor.controller.StartEditorController.java
* org.n52.smartsensoreditor.cswclient.facades
  * CSWContextSOS.java </br>
    original file: de.conterra.smarteditor.cswclient.facades.CSWContext.java
  * TransactionResponseSOS.java </br>
    original file: de.conterra.smarteditor.cswclient.facades.TransactionResponse.java
* org.n52.smartsensoreditor.cswclient.util
  * DefaultsSOS.java </br>
    original file: de.conterra.smarteditor.cswclient.util.Defaults.java
* org.n52.smartsensoreditor.dao
  * EditorAwareCatalogServiceDAO.java </br>
    original file: de.conterra.smarteditor.dao.CatalogServiceDAO.java
  * SOSCatalogService.java </br>
    original file: de.conterra.smarteditor.dao.GenericCatalogService.java
* org.n52.smartsensoreditor.service
  * BackendManagerServiceSML.java </br>
   original file: de.conterra.smarteditor.service.BackendManagerService.java
* org.n52.smartsensoreditor.validator
  * MetadataValidatorSML.java </br>
    original file: de.conterra.smarteditor.validator.MetadataValidator.java
* org.n52.smartsensoreditor.xml
  * EditorContextSML.java </br>
    original file: de.conterra.smarteditor.xml.EditorContext.java

The following files of the smartEditor-webapp sub project are needed to be modified or were created: </br>
resources folder:
* new groovy beans in ``resources/groovy``
* new xsl file extractDataFromSOSDescribeSensorResponse.xsl for tansformation of incoming SOS data in ``resources/internal/external``
* new xslt files for creating SOAP requests in ``resources/requests`
* new template files in ``resources/templates``
* new XML files for validation in ``resources/validation``
* new XSLT files to transform between bean and document in ``resources/xslt``
* adapt ``resources/codelist_enumeration.xml``, ``resources/isolist.properties``, and ``resources/isolist_de.properties`` for a new sensor button 
* adapt ``resources/messages.properties`` and ``resources/messages_de.properties`` for error messages, element labels and validation errors
* adapt ``resources/application.properties`` to define SOS Operations needed for the RequestFactory and the operationSOSManager states Beans
* adapt ``resources/log4j.xml`` to add a smartSensorEditor logger

webapp folder:
* new images for the tootips in ``webapp/images``
* new JavaScript files for highlighting the form fields in ``webapp/js/validation``
* adapt the standard.css in ``webapp/styles`` for the SOS extension
* new tooltip files in ``webapp/tooltips``

webapp/WEB-INF folder:
* adapt ``webapp/WEB-INF/defs/tiles-editor.xml`` to declare the .jsp files for each form element
* adapt ``webapp/WEB-INF/defs/tiles-elements.xml`` to define the .jsp files for multi form elements
* adapt ``webapp/WEB-INF/beans-definitions.xml`` to define the beans of the form elements and declare them in the ``backendBean``
* adapt ``webapp/WEB-INF/dao-definitions.xml`` for the SOS extension
* adapt ``webapp/WEB-INF/dispatcher-servlet.xml`` to change the bean declarations
* adapt ``webapp/WEB-INF/service-definitions.xml`` to declare the validators and set the ``activeBeanNamesRegex``
* adapt ``webapp/WEB-INF/util-config-definitions.xml`` to define additional information for the SmartSensorEditor separate
* adapt ``webapp/WEB-INF/validator-definitions.xml`` to define the new validators


webapp/WEB-INF/jsp folder:
* new jsp-files for the different form elements in ``webapp/WEB-INF/jsp/elements``
* adapt the tabs of the start page in ``webapp/WEB-INF/jsp/start``
* new ``webapp/WEB-INF/jsp/body_sensor.jsp`` and ``webapp/WEB-INF/jsp/body_acousticSensor.jsp`` to define which elements should contain the form
* adapt `webapp/WEB-INF/jsp/finished.jsp`` for the error message and the case that the page is shown after the "web- service" start tab
* adapt ``webapp/WEB-INF/jsp/menu.jsp`` to show the right error message
* adapt ``webapp/WEB-INF/jsp/selectStates.jsp`` for updating and inserting the sensor description at the SOS


### Build sub modules of the project

If only one of the sub modules should be build with maven, then go to the main folder of the whole project. With the attribute -pl a specific modul can be selected:

```
mvn clean package -pl smartsensoreditor-webapp
```

With the following command all dependencies of the module are build as well ([details](http://blog.sonatype.com/2009/10/maven-tips-and-tricks-advanced-reactor-options)):

```
mvn clean package -pl smartsensoreditor-webapp -am
```


### How to start the integration tests?

By default no integration tests are executed during the command "clean install". The integration test checks the communication between the SmartSensorEditor and the SOS. The following operations are checked:
<p>get/insert/update/delete sensor description.</p>
If you would like to execute them, then you need to create a build.properties file. You have to define the service url and the authorization token of the SOS. It should look like that:
<p>
IT_serviceURL:http://localhost:XX/XX/service </br>
IT_authorizationToken:XXX 
</p>
This propwerty file should be put into your home folder (/home/person for linux).
To build the maven project and execute the integration test go to the main folder of the SmartSensorEditor project. With the attribute -P a specific Profile can be chosen. 
Use the following command to start the integration tests for the SmartSensorEditor:

```
mvn -P itestsos clean install
```


### How can I adjust the editor?

Please see the [smartEditor developer documentation](https://wiki.52north.org/bin/view/Metadata/SmartEditorDeveloperDocumentation). 

## Contact

If you have further questions, contact Daniel: @nuest, d.nuest@52north.org

## License

The software is released under the Apache License version 2. (http://www.apache.org/licenses/LICENSE-2.0)

For used libraries see the `NOTICE` file.
