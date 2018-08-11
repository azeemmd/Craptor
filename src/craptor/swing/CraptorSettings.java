package craptor.swing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CraptorSettings {
	private static Properties dbtypes;

	public static Properties getDbtypes() {
		if(dbtypes == null)
		{
			try {
				dbtypes = new Properties();
				dbtypes.load(new FileInputStream("connection.properties"));

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dbtypes;
	}

}
