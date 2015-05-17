package edu.usf.experiment.subject;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3f;

import edu.usf.experiment.robot.Robot;
import edu.usf.experiment.subject.affordance.Affordance;
import edu.usf.experiment.utils.ElementWrapper;

public abstract class Subject {

	private String name;
	private String group;
	private Robot robot;
	private boolean hasEaten;
	private boolean triedToEat;

	public Subject(String name, String group, ElementWrapper modelParams, Robot robot) {
		this.name = name;
		this.group = group;
		this.robot = robot;
		
		hasEaten = false;
		triedToEat = false;
	}

	/**
	 * Returns the name of the subject
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setName(String subjectName) {
		this.name = subjectName;
	}
	
	/**
	 * Advances one cycle in the internal model of the brain usually resulting
	 * in a decision being taken
	 */
	public abstract void stepCycle();

	/**
	 * Returns whether the subject has eaten in the last iteration
	 * @return
	 */
	public boolean hasEaten(){
		return hasEaten;
	}
	
	/**
	 * Returns true if the subject has tried to eat, regardless of whether it could eat or not
	 * @return
	 */
	public boolean hasTriedToEat(){
		return triedToEat;
	}
	
	public void setTriedToEat(){
		triedToEat = true;
	}

	public void clearTriedToEAt(){
		triedToEat = false;
	}
	
	public Robot getRobot() {
		return robot;
	}

	public abstract void setPassiveMode(boolean b);

	public abstract List<Affordance> getPossibleAffordances();

	public abstract float getMinAngle();
	
	public abstract float getStepLenght();
	
	public abstract void newEpisode();
	
	public abstract void newTrial();

	public void setHasEaten(boolean b) {
		hasEaten = b;
	}
	
	public abstract Affordance getHypotheticAction(Point3f pos, float theta, int intention);

	public abstract void deactivateHPCLayers(LinkedList<Integer> indexList);

	public abstract void setExplorationVal(float i);

	public abstract void restoreExploration();
}
