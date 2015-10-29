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

```
cd smartSensorEditor
mvn clean install
```
If the feature for storing templates should be available, then additionally a database has to be created. For more information please take a look at the following link: https://wiki.52north.org/bin/view/Metadata/SmartEditorInstallationGuide#Database_preparation

Next you have to make sure that the needed runtime environment (Java and Tomcat) are properly installed and deploy the file ``<your workspace>/target/sensorSmartEditor.war``, start Tomcat, and open the browser at http://localhost:8080/smartSensorEditor.

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

* ``de.conterra.smarteditor.controller.SaveLocalController``
  * make XPath to generate the file id configurable
  * could go into smartEditor easily
* ``de.conterra.smarteditor.service.BackendManagerService``
  * method ``public String getResourceType()`` adapted to get to know if the resource type is sensor.
  * new method ``public boolean isBeanActive(String beanName)`` created to get sure that only the needed beans are merged with the document based on a [regular expression](http://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html) in the field ``activeBeanNamesRegex``
//  * method ``public String getFileIdentifier()`` adapted to get the Id from the BaseBean "smlUniqueID" when the sensorML     form is used.
//  * method ``public void newMetadataIdentifier()`` to set the new ID for the smlUniqueID bean.
* ``de.conterra.smarteditor.xml.EditorContext``
  * method ``public String getNamespaceURI(String prefix)`` and method ``public String getPrefix(String namespace)`` adopted for SensorML namespace
* ``org.n52.smartsensoreditor.beans.BackendBeanSML``
  * TODO
* ``org.n52.smartsensoreditor.beans.PublishBeanSML``
  * TODO
* ``org.n52.smartsensoreditor.controller.StartEditorController``
  * TODO

The following files of the smartEditor-webapp sub project are needed to be modified or were created:

* new groovy beans in ``resources/groovy``
* new file ``resources/internal/SimpleCopy.xslt``
* new template sensor.xml in ``resources/templates``
* new XML file for validation in ``resources/validation``
* new XSLT files to transform between bean and document in ``resources/xslt``
* adapt ``resources/codelist_enumeration.xml``, ``resources/isolist.properties``, and ``resources/isolist_de.properties`` for a new sensor button 
* adapt ``resources/messages.properties`` and ``resources/messages_de.properties`` for error messages, element labels and text about validation in 
* new JavaScript file for highlighting the form fields in ``webapp/js/validation``
//* create build.properties to declare the database connection in webapp/META-INF
//* create file log4j.xml to define the logging variables in webapp/WEB-INF/classes
* adapt ``webapp/WEB-INF/defs/tiles-editor.xml`` and ``body_sensor.jsp`` to declare the .jsp files for each form element
* adapt ``webapp/WEB-INF/defs/tiles-elements.xml`` to define the .jsp files for multi form elements
* new jsp-files for the different form elements in ``webapp/WEB-INF/jsp/elements``
* new ``webapp/WEB-INF/jsp/body_sensor.jsp`` to define which elements should contain the form in 
* adapt ``webapp/WEB-INF/jsp/menu.jsp`` to show the right error message
* adapt ``webapp/WEB-INF/beans-definitions.xml`` to define the beans of the form elements and declare them in the ``backendBean``
* adapt ``webapp/WEB-INF/service-definitions.xml`` to declare the validator and set the ``activeBeanNamesRegex``
* adapt ``webapp/WEB-INF/validator-definitions.xml`` to define new validator


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

By default no integration tests are executed during the command "clean install". If you would like to execute them, go to the main folder of the whole project. With the attribute -P a specific profile can be chosen.

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
