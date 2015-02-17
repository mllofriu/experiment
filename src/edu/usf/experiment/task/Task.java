package edu.usf.experiment.task;

import edu.usf.experiment.Episode;
import edu.usf.experiment.Experiment;
import edu.usf.experiment.Trial;

public interface Task {

	void perform(Experiment experiments);

	void perform(Trial trial);

	void perform(Episode episode);

}
