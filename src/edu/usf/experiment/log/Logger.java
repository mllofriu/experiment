package edu.usf.experiment.log;


import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;

public interface Logger {

	public void log(Episode episode);

	public void finalizeLog();

}
