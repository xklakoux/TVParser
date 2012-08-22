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
public class ParseWebsite extends Thread {

	Source source;
	String sourceUrlString;
	public final static int PROGRAM = 0;
	public final static int CHANNELS = 1;
	int doWhat;


	public static Logger logger = Logger.getLogger(ParseWebsite.class);

	Hashtable<String,String> channels = new Hashtable<String,String>();

	public ParseWebsite(String sourceUrlString, int doWhat) {
		super();
		this.doWhat = doWhat;
		this.sourceUrlString = sourceUrlString;
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
	}

	@Override
	public void run() {
		super.run();

		// List<? extends Segment> segments = source
		// .getAllElements(HTMLElementName.H3);

		// displaySegments(segments);
		if (doWhat == 1) {
			channels = getAllChannels();
			try {
				DBHandler.getInstance().insertChannels(channels);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			categories = getAllCategories();

		} else {
			String time=null;
			String title=null;
			String desc=null;
			for(Segment segment: source.getAllElementsByClass("p\\d\\d.*")){
				logger.info(segment.getFirstElementByClass("time").getContent().toString());
				time = segment.getFirstElementByClass("time").getContent().toString();
				logger.info(segment.getFirstElement(HTMLElementName.A).getContent().toString());
				title = segment.getFirstElement(HTMLElementName.A).getContent().toString();
				if(segment.getFirstElement(HTMLElementName.P)!=null){
					logger.info(segment.getFirstElement(HTMLElementName.P).getContent().toString());
					desc = segment.getFirstElement(HTMLElementName.P).getContent().toString();
				}
				try {
					DBHandler.getInstance().insertProgram(title, desc, time);
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
