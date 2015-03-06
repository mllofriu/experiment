package edu.usf.experiment.log;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import edu.usf.experiment.Episode;
import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;

public abstract class Logger {
	
	private static Map<String, PrintWriter> writers = new HashMap<String, PrintWriter>();

	public Logger(ElementWrapper params){
		
	}

	public abstract void log(Episode episode);

	public abstract void finalizeLog();
	
	public PrintWriter getWriter() {
		PrintWriter writer = null;
		
		if (!writers.containsKey(getFileName())) {
			try {
				// Writer with auto flush
				String logDir = PropertyHolder.getInstance().getProperty("log.directory");
				writer = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(new File(logDir + getFileName()))),
						true);
				writer.println(getHeader());
				writers.put(getFileName(), writer);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			writer = writers.get(getFileName());
		}

		return writer;
	}

	public abstract String getHeader();

	public abstract String getFileName();

}
