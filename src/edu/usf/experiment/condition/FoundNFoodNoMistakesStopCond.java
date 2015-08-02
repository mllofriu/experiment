package edu.usf.experiment.condition;

import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.Debug;
import edu.usf.experiment.utils.ElementWrapper;

public class FoundNFoodNoMistakesStopCond implements Condition {

	private int n;
	private int toGo;
	private boolean pardoned;

	public FoundNFoodNoMistakesStopCond(ElementWrapper condParams) {
		n = condParams.getChildInt("n");
		toGo = n;
		pardoned = false;
	}

	@Override
	public boolean holds(Episode episode) {
		Subject sub = episode.getSubject();

		if (sub.hasEaten()) {
			toGo--;
			pardoned = false;
		} else if (sub.hasTriedToEat() && pardoned){
			toGo = n;
			pardoned = false;
		} else if (sub.hasTriedToEat() && !pardoned)
			pardoned = true;

		
		if (Debug.printFoundNNoMistakes)
			System.out.println("Feeders to go " + toGo);

		return toGo <= 0;
	}

}
