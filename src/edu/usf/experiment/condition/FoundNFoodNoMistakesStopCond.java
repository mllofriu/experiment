package edu.usf.experiment.condition;

import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.Debug;
import edu.usf.experiment.utils.ElementWrapper;

public class FoundNFoodNoMistakesStopCond implements Condition {

	private int n;
	private int toGo;

	public FoundNFoodNoMistakesStopCond(ElementWrapper condParams) {
		n = condParams.getChildInt("n");
		toGo = n;
	}

	@Override
	public boolean holds(Episode episode) {
		Subject sub = episode.getSubject();

		if (sub.hasEaten()) {
			toGo--;
		} else if (sub.hasTriedToEat())
			toGo = n;

		
		if (Debug.printFoundNNoMistakes)
			System.out.println("Feeders to go " + toGo);

		return toGo <= 0;
	}

}
