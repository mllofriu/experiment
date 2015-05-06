package edu.usf.experiment.task;

import java.util.List;
import java.util.Random;

import edu.usf.experiment.Episode;
import edu.usf.experiment.Experiment;
import edu.usf.experiment.Trial;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.task.Task;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.utils.ElementWrapper;

/**
 * Task to deactivate a random feeder from the set of enabled ones
 * 
 * @author ludo
 * 
 */
public class TimeoutFlashFeeder extends Task {

	private Random random;
	private int timeout;
	private int timeSinceAte;

	public TimeoutFlashFeeder(ElementWrapper params) {
		super(params);
		random = new Random();
		timeout = params.getChildInt("timeout");
		timeSinceAte = 0;
	}

	@Override
	public void perform(Experiment experiment) {
		perform(experiment.getSubject(), experiment.getUniverse());
	}

	@Override
	public void perform(Trial trial) {
		perform(trial.getSubject(), trial.getUniverse());
	}

	@Override
	public void perform(Episode episode) {
		perform(episode.getSubject(), episode.getUniverse());
	}

	private void perform(Subject s, Universe u) {
		if (s.hasEaten())
			timeSinceAte = 0;
		else
			timeSinceAte++;

		if (timeSinceAte == timeout) {
			List<Integer> active = u.getActiveFeeders();

			// Pick an active one and flash
			int toFlash = active.get(random.nextInt(active.size()));
			u.setFlashingFeeder(toFlash, true);
		}
	}

}
