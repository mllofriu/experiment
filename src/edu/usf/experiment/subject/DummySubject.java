package edu.usf.experiment.subject;

import java.util.List;

import javax.vecmath.Point3f;

import edu.usf.experiment.robot.Robot;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.affordance.Affordance;
import edu.usf.experiment.utils.ElementWrapper;

public class DummySubject extends Subject {

	public DummySubject(String name, String group, ElementWrapper modelParams,
			Robot robot) {
		super(name, group, modelParams, robot);
		System.out.println("Dummy subject created");
	}

	@Override
	public boolean hasEaten() {
		return false;
	}

	@Override
	public void stepCycle() {
	}

	@Override
	public boolean hasTriedToEat() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPassiveMode(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Affordance> getPossibleAffordances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getMinAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void newEpisode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newTrial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Affordance getHypotheticAction(Point3f pos, float theta,
			int intention) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
