package edu.usf.experiment.universe;

import javax.vecmath.Point3f;

public class Wall {

	private Point3f p1;
	private Point3f p2;

	public Wall(Point3f p1, Point3f p2) {
		super();
		this.p1 = p1;
		this.p2 = p2;
	}

	public Point3f getP1() {
		return p1;
	}

	public void setP1(Point3f p1) {
		this.p1 = p1;
	}

	public Point3f getP2() {
		return p2;
	}

	public void setP2(Point3f p2) {
		this.p2 = p2;
	}

}
