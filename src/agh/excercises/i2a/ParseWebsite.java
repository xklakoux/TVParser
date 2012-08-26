package agh.excercises.i2a;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.html.HTMLElement;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.HTMLElements;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

/**
 * The Class ParseWebsite.
 * 
 */
public class ParseWebsite implements Runnable {

	Source source;
	String sourceUrlString;
	public final static int PROGRAM = 0;
	public final static int CHANNELS = 1;
	private int doWhat;
	


	public static Logger logger = Logger.getLogger(ParseWebsite.class);

	Hashtable<String,String> channels = new Hashtable<String,String>();

	public ParseWebsite(String sourceUrlString, int doWhat) {
		super();
		this.doWhat = doWhat;
		this.sourceUrlString = sourceUrlString;
	
	}

	@Override
	public void run() {
		try {
			if (sourceUrlString.indexOf(':') == -1)
				sourceUrlString = "file:" + sourceUrlString;
			source = new Source(new URL(sourceUrlString));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doWhat == ParseWebsite.CHANNELS) {
			channels = getAllChannels();
			try {
				DBHandler.getInstance().insertChannels(channels);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			categories = getAllCategories();

		} else {
			
			logger.info("Starting to parse "+ sourceUrlString);
			String time=null;
			String title=null;
			String desc=null;
			String date=source.getFirstElementByClass("list-day").getFirstElementByClass("act").getFirstElement(HTMLElementName.A).getAttributeValue("href").split("=")[1];
			logger.info("data -> " + date);
			for(Segment segment: source.getAllElementsByClass("p\\d\\d.*")){
				logger.debug(segment.getFirstElementByClass("time").getContent().toString());
				time = segment.getFirstElementByClass("time").getContent().toString();
				logger.debug(segment.getFirstElement(HTMLElementName.A).getContent().toString());
				title = segment.getFirstElement(HTMLElementName.A).getContent().toString();
				if(segment.getFirstElement(HTMLElementName.P)!=null){
					logger.debug(segment.getFirstElement(HTMLElementName.P).getContent().toString());
					desc = segment.getFirstElement(HTMLElementName.P).getContent().toString();
				}
				try {
					DBHandler.getInstance().insertProgram(title, desc, time, date);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static void displaySegments(List<? extends Segment> segments) {
		for (Segment segment : segments) {
			System.out
					.println("-------------------------------------------------------------------------------");
			System.out.println(segment.getFirstElement().getContent()
					.toString());

		}
		System.out
				.println("\n*******************************************************************************\n");
	}

	private Hashtable<String, String> getAllChannels() {
		Hashtable<String, String> channels = new Hashtable<String,String>();
		String name;
		String id;
		
		logger.debug("Parsing channels");
		for (Segment segment : source.getAllElementsByClass("entry")) {
			for (Segment seg : segment.getAllElements(HTMLElementName.LI)) {
				name = seg.getFirstElement(HTMLElementName.LABEL)
						.getContent().toString();
				id = seg.getFirstElement(HTMLElementName.INPUT).getAttributeValue("value").toString();
				channels.put(id,name);
				
				
			}
			
		}
		return channels;
	}

	private Vector<String> getAllCategories() {
		Vector<String> categories = new Vector<String>();
		// logger.debug("entrys "
		// +source.getAllElementsByClass("entry").size());
		logger.debug("Parsing categories");
		for (Segment segment : source.getAllElementsByClass("entry")) {
			for (Segment seg : segment.getAllElements(HTMLElementName.H3)) {
				categories.add(seg.getFirstElement().getContent().toString());
			}
		}
		logger.debug("Got " + categories.size() + "elements");
		return categories;
	}
	

	

}
