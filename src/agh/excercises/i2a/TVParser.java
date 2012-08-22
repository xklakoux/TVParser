package agh.excercises.i2a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

import net.htmlparser.jericho.*;

public class TVParser {

	
	static private MyProperties props;
	static Logger logger = Logger.getLogger(TVParser.class);
	
	static public void main(String[] argv){
		props = MyProperties.getInstance();
		
		BasicConfigurator.configure();
		logger.info("Entering application.");
//		System.out.println("2 numbers pls: ");
//		String name;
//		BufferedReader reader;
//        reader = new BufferedReader(new InputStreamReader(System.in));
//        try {
//			name = reader.readLine();
//			logger.info(Arrays.toString(name.split(" ")));
//        } catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//        return;
		
		//Downloader downloader = new Downloader(getProgramWebsite(getDate(DAY),canals),"program");
		Downloader downloader = new Downloader(getProgramWebsite(getDate(Integer.valueOf(props.day)),new String[]{props.channel}),"program");

		
		new Thread(downloader).start();
		logger.info("Exiting application.");
		
	};
	
	public static String getDate(int dayForward){
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String currDate = formatter.format(date);
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(formatter.parse(currDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.add(Calendar.DATE,dayForward);
		return formatter.format(c.getTime());

	}
	
	public static String getProgramWebsite(String date, String[] canals){
		
		StringBuilder builder = new StringBuilder();
		for(String canal: canals){
			builder.append("&k="+canal);
		}
		
		return props.address_programs + "/?den=" + date + new String(builder);
	}
	

	
}