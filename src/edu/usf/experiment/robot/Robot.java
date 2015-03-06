package edu.usf.experiment.robot;

import java.util.List;

import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.affordance.Affordance;
import edu.usf.experiment.utils.ElementWrapper;

/**
 * This class represents the body of the subject. It allows it to make movements and query information.
 */

/**
 * @author gtejera, mllofriu
 * 
 */
public abstract class Robot {
	
	public Robot(ElementWrapper params){
		
	}
	
	/**
	 * Makes the robot eat food
	 */
	public abstract void eat();

	/**
	 * Return whether the robot has found food in the environment
	 * 
	 * @return
	 */
	public abstract boolean hasFoundFood();

	/**
	 * Method invocked at the beginning of each session
	 */
	public abstract void startRobot();

	/**
	 * Move forward one step
	 */
	public abstract void forward(float distance);
	
	/**
	 * Makes the robot perform an action.
	 * 
	 * @param degrees
	 *            If degrees == 0, the robot goes forward. Else, it turns the
	 *            amount number of degrees. Negative degrees represent left
	 *            turns.
	 */
	public abstract void rotate(float degrees);

	/**
	 * Get a list of all visible landmarks
	 * @return
	 */
	public abstract List<Landmark> getLandmarks();

	public abstract Landmark getFlashingFeeder();

	public abstract boolean seesFlashingFeeder();

	public abstract Landmark getClosestFeeder(int lastFeeder);

	public abstract boolean isFeederClose();

	/**
	 * Checks each passed affordance to decide if it is realizable or not
	 * @param possibleAffordances
	 * @return
	 */
	public abstract List<Affordance> checkAffordances(List<Affordance> possibleAffordances);

	public abstract void executeAffordance(Affordance selectedAction, Subject sub);
}
