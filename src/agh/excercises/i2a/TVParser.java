package agh.excercises.i2a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

import net.htmlparser.jericho.*;

public class TVParser {

	static private MyProperties props;
	static Logger logger = Logger.getLogger(TVParser.class);
	private static DownloaderPool pool;
	private static ArrayList<Runnable> list;

	static public void main(String[] argv) {
		String[] days = {"0","1","2"};
		props = MyProperties.getInstance();
		BlockingQueue queue = new LinkedBlockingQueue<Runnable>();

		pool = new DownloaderPool(0, 10, 5000, TimeUnit.SECONDS, queue);
		list = new ArrayList<Runnable>();
		
		BasicConfigurator.configure();
		logger.info("Entering application.");

		Downloader downloader = new Downloader(props.address_channels,"channels");
		list.add(downloader);
		ParseWebsite parse = new ParseWebsite("channels",ParseWebsite.CHANNELS);
		list.add(parse);
		
		for(String day: days){
			downloader = new Downloader(getProgramWebsite(
					getDate(Integer.valueOf(day)),
					new String[] { props.channel}),"stanice");
			list.add(downloader);
			parse = new ParseWebsite("stanice", ParseWebsite.PROGRAM);
			list.add(parse);
		}
		
		pool.addAll(list);

		logger.info("Exiting main application.");

	};

	public static String getDate(int dayForward) {

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
		c.add(Calendar.DATE, dayForward);
		return formatter.format(c.getTime());

	}

	public static String getProgramWebsite(String date, String[] canals) {

		StringBuilder builder = new StringBuilder();
		for (String canal : canals) {
			builder.append("&k=" + canal);
		}

		return props.address_programs + "/?den=" + date + new String(builder);
	}

}