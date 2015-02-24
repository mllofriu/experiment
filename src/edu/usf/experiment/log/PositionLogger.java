package edu.usf.experiment.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3f;

import edu.usf.experiment.Episode;
import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;

public abstract class PositionLogger implements Logger {

	private List<Pose> poses;
	private static PrintWriter writer = null;
	
	public PositionLogger(ElementWrapper params){
		poses = new LinkedList<Pose>();
	}

	public void finalizeLog() {
		PropertyHolder props = PropertyHolder.getInstance();
		String trialName = props.getProperty("trial");
		String groupName = props.getProperty("group");
		String subName = props.getProperty("subject");
		String episode = props.getProperty("episode");
		
		synchronized (PositionLogger.class) {
			PrintWriter writer = getWriter();
			for (Pose pose : poses)
				writer.println(trialName + '\t' + groupName + '\t' + subName
						+ '\t' + episode + '\t' + pose.x + "\t" + pose.y + "\t"
						+ pose.randomAction);
			poses.clear();
		}
	}

	private PrintWriter getWriter() {
		if (writer == null) {
			try {
				// Writer with auto flush
				String logDir = PropertyHolder.getInstance().getProperty("log.directory");
				writer = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(new File(logDir + getFileName()))),
						true);
				writer.println("trial\tgroup\tsubject\trepetition\tx\ty\trandom");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return writer;
	}

	public abstract String getFileName();

	public void addPose(Pose p){
		poses.add(p);
	}
	
	@Override
	public abstract void log(Episode episode);

}

