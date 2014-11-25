package org.neu.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.xmlmatchers.XmlMatchers.*;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

public class TestHelloWorld {

	@Test
	public void testStatement() {
		HelloWorld hello = new HelloWorld();
		assertThat(hello.getStatement("Test"), equalTo("Hello Test"));
	}

	@Test
	public void testSensorXML() {
		Source source = new StreamSource(new StringReader(
				"<mountains><mountain>mtn</mountain></mountains>"));
		assertThat(source, hasXPath("/mountains/mountain"));
	}

}
