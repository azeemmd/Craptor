/*
 * Reads the XMl and generates the connection objects(Not the physical connections)
 */
package craptor.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import craptor.core.EnvConnection;

public class ConnectionsFileHandler {
    private javax.xml.parsers.DocumentBuilder parser;
    private Document document;
    private String fileName;
    
    private ConnectionsFileHandler(String fileName) throws Exception {
        this.fileName = fileName;
        // Get a JAXP parser factory object
        javax.xml.parsers.DocumentBuilderFactory dbf =
        DocumentBuilderFactory.newInstance();
        // Tell the factory what kind of parser we want 
        dbf.setValidating(false);
        // Use the factory to get a JAXP parser object
        parser = dbf.newDocumentBuilder();

        // Tell the parser how to handle errors.  Note that in the JAXP API,
        // DOM parsers rely on the SAX API for error handling
        parser.setErrorHandler(new org.xml.sax.ErrorHandler() {
        public void warning(SAXParseException e) {
            System.err.println("WARNING: " + e.getMessage());
        }
        public void error(SAXParseException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        public void fatalError(SAXParseException e)
           throws SAXException {
             System.err.println("FATAL: " + e.getMessage());
              throw e;   // re-throw the error
            }
        });
        
        //Parse the xml file and get the document
        document = parser.parse(new File(fileName));
    }
    
    public static ConnectionsFileHandler getInstance(String fileName) throws Exception {
        return new ConnectionsFileHandler(fileName);
    }
    /**
     * Traverse through document and creates the connection objects
     * @return
     * @throws Exception
     */
    public List<EnvConnection> getConnections() throws Exception {
        List<EnvConnection> connections = new ArrayList<EnvConnection>();
        
        Node node = document.getFirstChild();
        NodeList nodes = node.getChildNodes(); // Gets the connection

        for(int i = 0; i < nodes.getLength(); ++i) // Loop through connection
        {  
            Node childNode = nodes.item(i);
            NamedNodeMap attr;
            if((attr = childNode.getAttributes()) != null){
                String connectionName = attr.getNamedItem("name").getTextContent();
                EnvConnection conn = new EnvConnection(connectionName);
                NodeList childNodes = childNode.getChildNodes();
                for(int j = 0; j < childNodes.getLength(); ++j) // Lop for host, port ..etc..
                {   
                    Node subChildNode = childNodes.item(j);
                    String nodeName = subChildNode.getNodeName();
                    String value = subChildNode.getTextContent().trim();
                    
                    if("host".equals(nodeName)) {
                        conn.setHost(value);
                    }
                    else if("port".equals(nodeName)) {
                        conn.setPort(value);
                    } 
                    else if("sid".equals(nodeName)) {
                        conn.setSid(value);
                    }
                    else if("username".equals(nodeName)) {
                        conn.setUserName(value);
                    }
                    else if("password".equals(nodeName)) {
                        conn.setPassword(value);
                    }
                    else if("url".equals(nodeName)) {
                        conn.setUrl(value);
                    }
                    else if("dbtype".equals(nodeName)) {
                        conn.setDbtype(value);
                    }
                }
                connections.add(conn);
            }
        }
        
        return connections;
    }
    /**
     * Adds the connection to document
     * @param conn
     */
    public void addConnection(EnvConnection conn){
        Element connection =  document.createElement("connection");
        connection.setAttribute("name",conn.getName());
        
        Element host = document.createElement("host");
        host.insertBefore(document.createTextNode(conn.getHost()), host.getFirstChild());
        
        Element port = document.createElement("port");
        port.insertBefore(document.createTextNode(conn.getPort()), port.getFirstChild());
        
        Element sid = document.createElement("sid");
        sid.insertBefore(document.createTextNode(conn.getSid()), sid.getFirstChild());
        
        Element username = document.createElement("username");
        username.insertBefore(document.createTextNode(conn.getUserName()), username.getFirstChild());
        
        Element password = document.createElement("password");
        password.insertBefore(document.createTextNode(conn.getPassword()), password.getFirstChild());
        
        Element url = document.createElement("url");
        url.insertBefore(document.createTextNode(conn.getUrl()), url.getFirstChild());
        
        Element dbType = document.createElement("dbType");
        dbType.insertBefore(document.createTextNode(conn.getDbtype()), dbType.getFirstChild());
        
        connection.insertBefore(host,connection.getFirstChild());
        connection.insertBefore(port,connection.getFirstChild().getNextSibling());
        connection.insertBefore(sid,connection.getFirstChild().getNextSibling());
        connection.insertBefore(username,connection.getFirstChild().getNextSibling());
        connection.insertBefore(password, connection.getFirstChild().getNextSibling());
        connection.insertBefore(url, connection.getFirstChild().getNextSibling());
        connection.insertBefore(dbType, connection.getFirstChild().getNextSibling());
        
        document.getFirstChild().appendChild(connection);
        try {
            saveFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Deletes the connections from document
     * @param connName
     */
    public void deleteConnection(String connName){
        NodeList connections = document.getElementsByTagName("connection");
        int len = connections.getLength();
        // try to remove this loop by placing ID attr in connection tag... document.getElementById
        for(int i = 0; i < len; ++i) {
            Node node = connections.item(i);
            System.out.println(node.getAttributes().getNamedItem("name"));
            String name = node.getAttributes().getNamedItem("name").getTextContent();
            
            if(connName.equals(name)){
                node.getParentNode().removeChild(node);
                break;
            }
        }
        document.normalize();
        try {
            saveFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves the document to the file
     * @throws FileNotFoundException
     */
    
    public void saveFile() throws FileNotFoundException {
        XMLDocumentWriter writer = new XMLDocumentWriter(new PrintWriter(fileName));
        writer.write(document);
        writer.close();
    }
    
    /*public static void main(String[] args) {
        ConnectionsFileHandler parser;
        try {
            parser = ConnectionsFileHandler.getInstance("connections1.xml");
            ArrayList list = parser.getConnections();
            for(int i = 0 ; i < list.size(); ++i) {
                System.out.println(((EnvConnection)list.get(i)).getConnectionString());
            }
            parser.deleteConnection("IGSD2RM");
            XMLDocumentWriter writer = new XMLDocumentWriter(new PrintWriter("connections1.xml"));
            writer.write(parser.document);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
