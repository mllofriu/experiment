package edu.usf.experiment.subject.affordance;

public class TurnAffordance extends Affordance {

	private float angle;
	private float distance;

	public TurnAffordance(float angle, float distance) {
		super();

		this.angle = angle;
		this.distance = distance;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getDistance() {
		return distance;
	}
}
