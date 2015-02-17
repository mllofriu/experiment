package edu.usf.experiment.log;

import edu.usf.experiment.Trial;

public interface ExperimentLogger {

	public void log(Trial trial);

	public void finalizeLog();

}
