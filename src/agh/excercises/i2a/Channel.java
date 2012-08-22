package agh.excercises.i2a;

import java.util.Hashtable;

public class Channel extends Hashtable<String, String>{
	public String id;
	public String name;
	Hashtable<String, String> hashtable;
	
	public Channel(String arg0, String arg1){
		hashtable = new Hashtable<String,String>();
		hashtable.put(arg0, arg1);
	}
	
}
