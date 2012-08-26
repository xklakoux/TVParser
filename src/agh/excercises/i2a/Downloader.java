//-Dhttp.agent="Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" <- pretend to be a browser

package agh.excercises.i2a;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.CookieManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.sql.*;

import org.apache.log4j.*;

public class Downloader implements Runnable {

	private String address;
	private String output = "stanice";
	private URL url;
	private File file = null;
	private MyProperties properties;

	static Logger logger = Logger.getLogger(Downloader.class);

	public Downloader(String address) {
		super();
		this.address = address;
	}

	public Downloader(URL url) {
		super();
		this.url = url;
	}

	public Downloader(MyProperties properties) {
		super();
		this.properties = properties;
	}

	public Downloader(String address, String outputHTML) {
		super();
		this.address = address;
		this.output = outputHTML;
	}

	public Downloader(URL url, File file) {
		super();
		this.url = url;
		this.file = file;
	}

	@Override
	public void run() {

		InputStream is = null;
		BufferedReader br;
		String line;
		FileWriter fstream = null;
		try {
			logger.info("Starting a Downloader thread");

			if (file == null) {
				file = new File(output);
			}

			fstream = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fstream);

			if (url == null) {
				url = new URL(address);
			}

			CookieManager manager = new CookieManager();
			CookieHandler.setDefault(manager);
			
//			CookieStore cookieJar = manager.getCookieStore();
			
//			HttpCookie cookie = new HttpCookie("foo", "bar");
//			try {
//				cookieJar.add(url.toURI(), cookie);
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}

			URLConnection postUrlConnection = url.openConnection();

			postUrlConnection.setReadTimeout(Integer.valueOf(MyProperties.getInstance().timeout));
			//postUrlConnection.setUseCaches(true);
			Map responseMap = postUrlConnection.getHeaderFields();
			// postUrlConnection = url.openConnection();
			for (Iterator iterator = responseMap.keySet().iterator(); iterator
					.hasNext();) {
				String key = (String) iterator.next();
				System.out.print(key + " = ");

				List values = (List) responseMap.get(key);
				for (int i = 0; i < values.size(); i++) {
					Object o = values.get(i);
					System.out.print(o + ", ");
				}
				System.out.println("");
			}

			is = postUrlConnection.getInputStream();

			int size = url.openConnection().getContentLength();

			is = url.openStream(); // throws an IOException

			logger.info("file size " + size);

			br = new BufferedReader(new InputStreamReader(is));

			long startTime = System.nanoTime();
			long diffTime;
			long fileSize = file.length();
			long diff;
			logger.debug("Starting to download");

			Random rand = new Random();
			new FutureTask<Downloader>(this,this);
			while ((line = br.readLine()) != null) {

				fstream.write(line + '\n');
				diff = file.length() - fileSize;
				diffTime = System.nanoTime() - startTime;
				if(rand.nextInt(20)<2){
				logger.info((float) diff / (diffTime / Math.pow(10, 6))
						+ " KB/s");
				// }
				}
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {

			try {
				if (is != null)
					is.close();
				if (fstream != null)
					fstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		logger.info("Downloaded " + address + " into " + output);

	}

	public File getFile() {
		return file;
	}

	public void setProperties(MyProperties properties) {
		this.properties = properties;
	}

}
