package agh.excercises.i2a;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.BufferedWriter;

public class MyProperties {
	static private MyProperties _instance = null;
	static public String address_channels = null;
	static public String address_programs = null;
	static public String channel = null;
	static public String day = null;
	static public String timeout = null;
	static public String reconnects = null;
	static public String login = null;
	static public String password = null;
	static public String db_name = null;
	static public String cookies = null;
	static public String errorStream = null;

	private MyProperties() {
		try {

			InputStream file = new FileInputStream(new File("tv.properties"));
			Properties props = new Properties();
			props.load(file);
			
			address_channels = props.getProperty("ADDRESS_CHANNELS");
			address_programs = props.getProperty("ADDRESS_PROGRAMS");
			channel = props.getProperty("CHANNEL");
			day = props.getProperty("DAY");
			timeout = props.getProperty("TIMEOUT");
			reconnects = props.getProperty("RECONNECTS");
			login = props.getProperty("LOGIN");
			password = props.getProperty("PASSWORD");
			db_name = props.getProperty("DATABASE_NAME");
			cookies = props.getProperty("COOKIES");
			errorStream = props.getProperty("ERROR_STREAM");

		} catch (Exception e) {
			
		}
	}

	static public MyProperties getInstance() {
		if (_instance == null) {
			_instance = new MyProperties();
		}
		return _instance;
	}
}
