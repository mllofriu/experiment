package edu.usf.experiment.robot;

import java.util.List;

import edu.usf.experiment.subject.affordance.Affordance;
import edu.usf.experiment.utils.ElementWrapper;

public class DummyRobot extends Robot {

	public DummyRobot(ElementWrapper params) {
		super(params);
	}

	@Override
	public void eat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasFoundFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startRobot() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void rotate(float degrees) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Landmark> getLandmarks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Landmark getFlashingFeeder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean seesFlashingFeeder() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isFeederClose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void forward(float distance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Landmark getClosestFeeder(int lastFeeder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Affordance> checkAffordances(
			List<Affordance> possibleAffordances) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeAffordance(Affordance selectedAction) {
		// TODO Auto-generated method stub
		
	}

}
