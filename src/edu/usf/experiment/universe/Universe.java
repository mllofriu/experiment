package edu.usf.experiment.universe;

import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;

import org.w3c.dom.Document;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.Debug;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;
import edu.usf.experiment.utils.XMLDocReader;

public abstract class Universe {

	private static final int WALLS_PER_POOL = 16;

	// TODO: get as param
	private static float CLOSE_TO_FOOD_THRS;

	private List<Feeder> feeders;
	private List<Wall> walls;
	private Rectangle2D.Float boundingRect;

	private int lastAteFeeder;


	public Universe(ElementWrapper params) {
		CLOSE_TO_FOOD_THRS = params.getChildFloat("closeToFoodThrs");

		String mazeFile = params.getChildText("maze");

		walls = new LinkedList<Wall>();
		feeders = new LinkedList<Feeder>();

		PropertyHolder props = PropertyHolder.getInstance();
		String dstMazeFile = props.getProperty("log.directory") + "maze.xml";
		IOUtils.copyFile(mazeFile, dstMazeFile);
		Document doc = XMLDocReader.readDocument(dstMazeFile);
		ElementWrapper maze = new ElementWrapper(doc.getDocumentElement());
		List<ElementWrapper> list;
		
		ElementWrapper brEW = maze.getChild("boundingRect");
		setBoundingRect(new Rectangle2D.Float(brEW.getChildFloat("x"),
				brEW.getChildFloat("y"), brEW.getChildFloat("w"),
				brEW.getChildFloat("h")));

		// list = doc.getElementsByTagName("pool");
		// if (list.getLength() != 0)
		// pool = new PoolNode(list.item(0));

		// Walls
		list = maze.getChildren("wall");
		for (ElementWrapper wall : list) {
			Wall w = new Wall(wall.getChildFloat("x1"),
					wall.getChildFloat("y1"), wall.getChildFloat("x2"),
					wall.getChildFloat("y2"));
			walls.add(w);
		}
		
		ElementWrapper pool = maze.getChild("pool");
		if (pool != null){
			float r = pool.getChildFloat("r");
			float x = pool.getChildFloat("x");
			float y = pool.getChildFloat("y");
			float currentAngle = 0;
			for (int i = 0; i < WALLS_PER_POOL; i++) {
				float x1 = (float) (x + r * Math.sin(currentAngle));
				float y1 = (float) (y + r * Math.cos(currentAngle));
				float nextAngle = (float) ((currentAngle + (2 * Math.PI / WALLS_PER_POOL)));
				float x2 = (float) (r * Math.sin(nextAngle));
				float y2 = (float) (r * Math.cos(nextAngle));
				
				walls.add(new Wall(x1, y1, x2, y2));
				
				currentAngle = nextAngle;
			}
		}
		
		list = maze.getChildren("feeder");
		int i = 0;
		for(ElementWrapper feeder : list){
			Feeder f = new Feeder(i, new Point3f(feeder.getChildFloat("x"),feeder.getChildFloat("y"),feeder.getChildFloat("z")));
			feeders.add(f);
			i++;
		}
		
	}

	// Boundaries
	public Rectangle2D.Float getBoundingRectangle(){
		return boundingRect;
	}

	// Robot position
	public abstract Point3f getRobotPosition();

	public abstract Quat4f getRobotOrientation();

	public abstract float getRobotOrientationAngle();

	// Feeders
	public Point3f getFoodPosition(int i) {
		return feeders.get(i).getPosition();
	}

	public List<Integer> getFlashingFeeders() {
		List<Integer> res = new LinkedList<Integer>();
		for (int i = 0; i < feeders.size(); i++)
			if (feeders.get(i).isFlashing())
				res.add(i);

		return res;
	}

	public List<Integer> getActiveFeeders() {
		List<Integer> res = new LinkedList<Integer>();
		for (int i = 0; i < feeders.size(); i++)
			if (feeders.get(i).isActive())
				res.add(i);

		return res;
	}

	public int getNumFeeders() {
		return feeders.size();
	}

	public void setActiveFeeder(int i, boolean val) {
		feeders.get(i).setActive(val);
	}

	public void setFlashingFeeder(int i, boolean flashing) {
		feeders.get(i).setFlashing(flashing);
	}

	public List<Integer> getFeeders() {
		List<Integer> res = new LinkedList<Integer>();
		for (int i = 0; i < feeders.size(); i++)
			res.add(i);

		return res;
	}
	
	public Feeder getFeeder(int i){
		return feeders.get(i);
	}

	public boolean isFeederActive(int feeder) {
		return feeders.get(feeder).isActive();
	}

	public boolean isFeederFlashing(int feeder) {
		return feeders.get(feeder).isFlashing();
	}

	public void releaseFood(int feeder) {
		feeders.get(feeder).releaseFood();
	}

	public boolean hasFoodFeeder(int feeder) {
		return feeders.get(feeder).hasFood();
	}

	// Involving position and food
	public boolean hasRobotFoundFood() {
		Point3f robot = getRobotPosition();
		for (Feeder f : feeders) {
			if (f.isActive() && f.hasFood()
					&& robot.distance(f.getPosition()) < CLOSE_TO_FOOD_THRS)
				return true;
		}

		return false;
	}

	public void robotEat() {
		int feedingFeeder = -1;

		Point3f robotPos = getRobotPosition();
		for (int i = 0; i < feeders.size(); i++) {
			if (robotPos.distance(feeders.get(i).getPosition()) < CLOSE_TO_FOOD_THRS)
				if (feeders.get(i).hasFood())
					feedingFeeder = i;
		}

		if (feedingFeeder != -1){
			feeders.get(feedingFeeder).clearFood();
			lastAteFeeder = feedingFeeder;
			if (Debug.printRobotEaten)
				System.out.println("Robot has eaten");
		} else {
			System.out.println("Robot tried to eat far from food");
		}
	}
	
	public int getLastFeedingFeeder(){
		return lastAteFeeder;
	}
	

	public boolean isRobotCloseToFeeder(int currentGoal) {
		Point3f robot = getRobotPosition();
		return robot.distance(feeders.get(currentGoal).getPosition()) < CLOSE_TO_FOOD_THRS;
	}

	public int getFeedingFeeder() {
		Point3f robotPos = getRobotPosition();
		for (int i = 0; i < feeders.size(); i++) {
			if (feeders.get(i).isActive())
				if (robotPos.distance(feeders.get(i).getPosition()) < CLOSE_TO_FOOD_THRS)
					return i;
		}

		return -1;
	}

	public boolean hasRobotFoundFeeder(int i) {
		Point3f robot = getRobotPosition();
		Feeder f = feeders.get(i);
		return robot.distance(f.getPosition()) < CLOSE_TO_FOOD_THRS;
	}

	public boolean isRobotCloseToAFeeder() {
		Point3f robot = getRobotPosition();
		for (Feeder f : feeders)
			if (robot.distance(f.getPosition()) < CLOSE_TO_FOOD_THRS)
				return true;
		return false;
	}

	public float getDistanceToFeeder(int i) {
		return getRobotPosition().distance(feeders.get(i).getPosition());
	}

	public int getFoundFeeder() {
		Point3f robot = getRobotPosition();
		for (Feeder f : feeders)
			if (robot.distance(f.getPosition()) < CLOSE_TO_FOOD_THRS)
				return feeders.indexOf(f);

		return -1;
	}

	public List<Integer> getEnabledFeeders() {
		List<Integer> res = new LinkedList<Integer>();
		for (Feeder f : feeders)
			if (f.isEnabled())
				res.add(feeders.indexOf(f));
		return res;
	}

	public void setEnableFeeder(Integer f, boolean enabled) {
		feeders.get(f).setEnabled(enabled);
	}

	public Rectangle2D.Float getBoundingRect() {
		return boundingRect;
	}

	public void setBoundingRect(Rectangle2D.Float boundingRect) {
		this.boundingRect = boundingRect;
	}

	public abstract void setRobotPosition(Float float1, float w);

	public List<Wall> getWalls() {
		return walls;
	}

}
