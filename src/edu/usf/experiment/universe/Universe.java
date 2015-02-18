package edu.usf.experiment.universe;

import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;

import com.vividsolutions.jts.geom.LineSegment;

public interface Universe {
	// Boundaries
	public Rectangle2D.Float getBoundingRectangle();
	
	// Robot position
	public Point3f getRobotPosition();
	
	public Quat4f getRobotOrientation();
	
	public float getRobotOrientationAngle(); 
	
	// Walls
	public void addWall(float x1, float y1, float x2, float y2);

	public void clearWalls();
	
	public List<Wall> getWalls();

	public boolean wallIntersectsOtherWalls(LineSegment wall);

	public float shortestDistanceToWalls(LineSegment wall);

	public boolean wallInsidePool(LineSegment wall2);

	public boolean placeIntersectsWalls(Polygon c);
	
	// Robot position and walls
	public boolean isRobotParallelToWall();
	
	// Feeders
	public Point3f getFoodPosition(int i);

	public List<Integer> getFlashingFeeders() ;

	public List<Integer> getActiveFeeders() ;

	public int getNumFeeders();
	
	public void setActiveFeeder(int i, boolean val) ;
	
	public void setFlashingFeeder(Integer i, boolean flashing) ;

	public int getFeedingFeeder();

	public List<Integer> getFeeders();

	public float wallDistanceToFeeders(LineSegment wall);

	public boolean isFeederActive(int feeder);
	
	public void releaseFood(int feeder);

	public boolean hasFoodFeeder(int feeder);
	
	// Involving position and food
	public boolean hasRobotFoundFood();
	
	public void robotEat();
	
	public boolean isRobotCloseToFeeder(int currentGoal);

	public int getFeedingFeeder();
	
	public boolean hasRobotFoundFeeder(int i);
	
	public int getFeederInFrontOfRobot(int excludeFeeder);
	
	public boolean isRobotCloseToAFeeder();
	
	public float getDistanceToFeeder(int i);
	
	public int getFoundFeeder();
	
	public float angleToFeeder(Integer fn);
	
}
