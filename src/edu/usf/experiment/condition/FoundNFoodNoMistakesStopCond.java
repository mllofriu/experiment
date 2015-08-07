package edu.usf.experiment.condition;

import java.util.LinkedList;
import java.util.List;

import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.Debug;
import edu.usf.experiment.utils.ElementWrapper;

public class FoundNFoodNoMistakesStopCond implements Condition {

	private int n;
	private int toGo;
	private List<Boolean> flashing;

	public FoundNFoodNoMistakesStopCond(ElementWrapper condParams) {
		n = condParams.getChildInt("n");
		toGo = n;
		flashing = new LinkedList<Boolean>();
	}

	@Override
	public boolean holds(Episode episode) {
		Subject sub = episode.getSubject();

		if (sub.hasEaten()) {
			flashing.add(sub.getRobot().getClosestFeeder().isFlashing());
			toGo--;
		} else if (sub.hasTriedToEat()) {
			toGo = n;
		}

		if (Debug.printFoundNNoMistakes)
			System.out.println("Feeders to go " + toGo);

		return toGo <= 0 && countFlashing(flashing, n) <= 2;
	}

	private int countFlashing(List<Boolean> flashing, int n) {
		List<Boolean> lastVisited = flashing.subList(flashing.size() - n,
				flashing.size() - 1);
		int count = 0;
		for (Boolean flash : lastVisited)
			if (flash)
				count++;

		return count;
	}

}
