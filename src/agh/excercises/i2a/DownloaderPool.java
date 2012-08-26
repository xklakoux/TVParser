package agh.excercises.i2a;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class DownloaderPool extends ThreadPoolExecutor{
	
	
	
	public DownloaderPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		// TODO Auto-generated constructor stub
	}

	public List<Runnable> stopNow(){
		return shutdownNow();
	}
	
	public ArrayList<Future> addAll(ArrayList<Runnable> list){
		ArrayList<Future> futures = new ArrayList<Future>();
		for (Runnable runn: list){
			Logger.getLogger(DownloaderPool.class).debug("adding runnable " + runn.toString());
			futures.add(submit(runn));
		}
		return futures;	
	}
	
}
