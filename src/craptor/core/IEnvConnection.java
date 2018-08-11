package craptor.core;

import java.sql.Connection;
import java.sql.SQLException;

public interface IEnvConnection {
	
	public String getName();
	public String getUserName();
	public String getPassword();
	public String toString();
	public void setHost(String host);
	public String getHost();
	public void setPort(String port);
	public String getPort();
	public void setName(String name);
	public void setUserName(String userName);
	public void setPassword(String password);
	public String getUrl();
	public void setUrl(String url);
	public String getDbtype();
	public void setDbtype(String dbtype);
	public Connection getConnection() throws ClassNotFoundException, SQLException;

}
