/*
 * SAX parser : Not Using 
 */


package craptor.parser;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import craptor.core.EnvConnection;

public class ConnectionsXMLParser extends DefaultHandler{

    private EnvConnection connection;
    private List<EnvConnection> connections;
    private String currentTag;
    public ConnectionsXMLParser() throws Exception {
      
    }
    
    public List<EnvConnection> parse(String fileName) throws Exception {
        connections = new ArrayList<EnvConnection>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse( new File(fileName), this ); 
        return connections;
    }
    
    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startElement(String uri, String localName, String qName,
                    Attributes atts) {
        if("connection".equals(qName)) {
            connection = new EnvConnection(atts.getValue("name"));
        }
        else {
            currentTag = qName;
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if("connection".equals(qName)) {
            connections.add(connection);
            connection = null;
        }
    }

    public void characters(char[] ch, int start, int length) {
        String value = new String(ch, start, length);
        if("host".equals(currentTag)) {
            connection.setHost(value);  
            currentTag = null;
        }
        else if("port".equals(currentTag)) {
            connection.setPort(value); 
            currentTag = null;
        }
        else if("sid".equals(currentTag)) {
            connection.setSid(value);
            currentTag = null;
        }
        else if("username".equals(currentTag)) {
            connection.setUserName(value); 
            currentTag = null;
        }
        else if("password".equals(currentTag)) {
            connection.setPassword(value);   
            currentTag = null;
        }
    }
    
    
   /* public static void main(String[] args) {
        ConnectionsXMLParser p;
        try {
            p = new ConnectionsXMLParser();
            ArrayList list = p.parse("connections.xml");
            for(int i = 0 ; i < list.size(); ++i) {
                System.out.println(((EnvConnection)list.get(i)).getConnectionString());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
