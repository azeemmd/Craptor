/*
 * 
 * Class to store the connection information
 */


package craptor.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import craptor.swing.CraptorSettings;

public class EnvConnection implements IEnvConnection{
    
    private String name;
    private String userName;
    private String password;    
    private String host;
    private String port;
    private String sid;
    private String url;
    private String dbtype;
    
    public EnvConnection(String name) {
        this.name = name;
    }
    
    public EnvConnection() {
        this("", "", "", "", "", "");
    }

    public EnvConnection(String name, String userName,
                         String password, String host, String port, String sid) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        this.sid = sid;
        this.dbtype = "oracle";
        setUrl();
    }
    
    public EnvConnection(String name, String userName, String password, String url, String dbtype)
    {
    	this.name = name;
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.dbtype = dbtype;    	
    }
    
    public Connection getConnection() throws ClassNotFoundException, SQLException
    {
    	loadDriver();
    	
    	if(url == null)
    		setUrl();
    	System.out.println(url);
        return 
             DriverManager.getConnection(url,userName,password);

    }
    
    public void setUrl()
    {
    	url = "jdbc:oracle:thin:@" + host.trim() + ":" + port.trim() + ":" + sid.trim();
    }
    
    public void loadDriver() throws ClassNotFoundException
    {
    	
    	Properties drivers = CraptorSettings.getDbtypes();
		String driverClass = drivers.getProperty(dbtype);
		Class.forName(driverClass);
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return getName();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
}
