package edu.usf.experiment.condition;

import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.utils.ElementWrapper;

public class FoundFoodStopCond implements Condition {

	public FoundFoodStopCond(ElementWrapper params) {
	}

	@Override
	public boolean holds(Episode episode) {
		Universe uni = episode.getUniverse();
		Subject sub = episode.getSubject();
		return uni.hasRobotFoundFood() && sub.hasEaten();
	}

}
