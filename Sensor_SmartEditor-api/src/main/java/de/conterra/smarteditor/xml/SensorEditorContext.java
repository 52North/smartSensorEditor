package de.conterra.smarteditor.xml;

import javax.xml.XMLConstants;

import de.conterra.smarteditor.xml.EditorContext;

public class SensorEditorContext extends EditorContext {

    protected static String sNS_SML="http://www.opengis.net/sensorML/1.0";

	public SensorEditorContext() {
		//
	}
	
    public String getNamespaceURI(String prefix) {
        if (prefix.equals("sml")) {
            return sNS_SML;
        } 
        return super.getNamespaceURI(prefix);
    }

    /**
     * Resolves to a prefix
     *
     * @param namespace
     * @return
     */
    public String getPrefix(String namespace) {
    	 if (namespace.equals(sNS_SML)) {
             return "sml";
         }
        	 return super.getPrefix(namespace);
         
    }
}
