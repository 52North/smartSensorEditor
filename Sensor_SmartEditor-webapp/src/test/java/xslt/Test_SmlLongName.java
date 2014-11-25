package xslt;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class Test_SmlLongName {

	
	@Test
	public void testSML(){
		//http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
		//http://stackoverflow.com/questions/10536183/resource-files-not-found-from-junit-test-cases
		File sensor;
	
		Document doc;
		try {	sensor = new File(getClass().getResource("/sensor.xml").toURI());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(sensor);
			doc.getDocumentElement().normalize();
		} catch (SAXException | IOException | ParserConfigurationException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
