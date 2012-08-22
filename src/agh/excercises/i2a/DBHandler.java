package agh.excercises.i2a;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public class DBHandler {

	public static final String DB_NAME = "tv.db";
	public static final String PROGRAMS_T = "programs";
	public static final String CATEGORIES_T = "categories";
	public static final String CHANNELS_T = "channels";
	private Connection conn = null;
	private static DBHandler instance = null;
	public static Logger logger = Logger.getLogger(DBHandler.class);
	public static String queryTime="select strftime('%Y-%m-%d','now','0 day');";


	public DBHandler() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
		Statement stat = conn.createStatement();
		String query = ""
				+ "DROP TABLE IF EXISTS " + CHANNELS_T + ";"
				+ "DROP TABLE IF EXISTS " + CATEGORIES_T + ";"
				+ "DROP TABLE IF EXISTS " + PROGRAMS_T + ";"
				+ "BEGIN TRANSACTION;"
				+ "CREATE TABLE "+CHANNELS_T+" (id integer primary key, name varchar(30));"
				+ "CREATE TABLE "+CATEGORIES_T+" (id integer primary key, name varchar(30));"
				+ "CREATE TABLE "+PROGRAMS_T+" (id integer primary key autoincrement, id_cha integer,"
				+ "name varchar(30), desc text, date timestamp, foreign key(id_cha) references "+CHANNELS_T+"(id));"
				+ "COMMIT;";
		logger.debug(query);
		stat.executeUpdate(query);

		
		
//		PreparedStatement prep = conn
//				.prepareStatement("insert into people values (?, ?);");
//
//		prep.setString(1, "Gandhi");
//		prep.setString(2, "politics");
//		prep.addBatch();
//		prep.setString(1, "Turing");
//		prep.setString(2, "computers");
//		prep.addBatch();
//		prep.setString(1, "Wittgenstein");
//		prep.setString(2, "smartypants");
//		prep.addBatch();
//		ResultSet rs = stat.executeQuery("select * from people;");
//		while (rs.next()) {
//			System.out.println("name = " + rs.getString("name"));
//			System.out.println("job = " + rs.getString("occupation"));
//		}
//		rs.close();
//		conn.close();
	}
	
	
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		conn.close();
	}



	public static DBHandler getInstance(){
		if(instance == null){
			try {
				instance = new DBHandler();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	public void insertChannels(Hashtable<String, String> channels) throws SQLException{
		
		Enumeration <String> e = channels.keys();
		
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);

		String key;	
		while(e.hasMoreElements()){
			key = e.nextElement();
			String query = "insert into " +CHANNELS_T+ "(id, name) values ("+key+","+"\'"+channels.get(key)+"\');";
//			logger.debug(query);
			stmt.addBatch(query); 
		}
		conn.setAutoCommit(true);
		int [] updateCounts = stmt.executeBatch();
		logger.debug("Update counts " +Arrays.toString(updateCounts));

	}
	
	//public void insertProgramms()
	
//	
//	public void insertCategories(Hashtable<String, String> categories) throws SQLException{
//		
//		Enumeration <String> e = categories.keys();
//		
//		Statement stmt = conn.createStatement();
//		conn.setAutoCommit(false);
//
//		String key;	
//		while(e.hasMoreElements()){
//			key = e.nextElement();
//			String query = "insert into " +CATEGORIES_T+ "(id, name) values ("+key+","+"\'"+categories.get(key)+"\');";
//			logger.debug(query);
//			stmt.addBatch(query); 
//		}
//		conn.setAutoCommit(true);
//		int [] updateCounts = stmt.executeBatch();
//		logger.debug("Update counts " +Arrays.toString(updateCounts));
//
//	}

	public void insertCategories(Hashtable<String, String> categories) throws SQLException{
		
		Enumeration <String> e = categories.keys();
		
		Statement stmt = conn.createStatement();
		conn.setAutoCommit(false);
	
		String key;	
		while(e.hasMoreElements()){
			key = e.nextElement();
			String query = "insert into " +CATEGORIES_T+ "(id, name) values ("+key+","+"\'"+categories.get(key)+"\');";
			logger.debug(query);
			stmt.addBatch(query); 
		}
		conn.setAutoCommit(true);
		int [] updateCounts = stmt.executeBatch();
		logger.debug("Update counts " +Arrays.toString(updateCounts));
	
	}
	
	public void insertProgram(String title, String desc, String time) throws SQLException{
		String canal = MyProperties.getInstance().channel;
		Statement stmt = conn.createStatement();
		String dbDate = TVParser.getDate(Integer.valueOf(MyProperties.getInstance().day))+" "+time;
		logger.debug("this date  --> " + dbDate);
		desc = (desc!=null?desc:"");
		String query = "Insert into "+PROGRAMS_T+" (id_cha, name, desc, date) values ("+canal+",'"+title+"','"+desc+"','"+dbDate+"');";
		logger.debug("DBQuery ->" + query);
		stmt.executeUpdate(query);
	}
	
}