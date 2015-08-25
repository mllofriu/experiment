package edu.usf.experiment.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point2f;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

import edu.usf.experiment.Episode;
import edu.usf.experiment.Experiment;
import edu.usf.experiment.Trial;
import edu.usf.experiment.robot.Robot;
import edu.usf.experiment.robot.RobotLoader;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.SubjectLoader;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.universe.UniverseLoader;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.RandomSingleton;
import edu.usf.experiment.utils.XMLExperimentParser;

public class AddSmallWallsTask extends Task {

	private final float RADIUS = .40f;
	private static final float MIN_DIST_TO_FEEDERS = 0.05f;
	private static final float NEAR_WALL_RADIUS = .45f;
	private static final float LENGTH = .13f;
	private static final int NUM_WALLS = 10;
	private static final float DISTANCE_INTERIOR_WALLS = .1f;
	private static final float MIN_DIST_TO_FEEDERS_INTERIOR = 0.1f;
	private static final double NUM_INTERIOR_WALLS = 4;

	public AddSmallWallsTask(ElementWrapper params) {
		super(params);

	}

	@Override
	public void perform(Experiment experiment) {
		perform(experiment.getUniverse(), experiment.getSubject());
	}

	@Override
	public void perform(Trial trial) {
		perform(trial.getUniverse(), trial.getSubject());
	}

	@Override
	public void perform(Episode episode) {
		perform(episode.getUniverse(), episode.getSubject());
	}

	private void perform(Universe univ, Subject sub) {
		Random random = RandomSingleton.getInstance();

		List<Point2f> nearWall;
		LineSegment wall;
		boolean noClosePoints;
		do {
			// Pick an orientation to seed the three walls near the outside
			float orientation = (float) (random.nextFloat() * Math.PI);
			nearWall = new LinkedList<Point2f>();
			noClosePoints = true;
			for (int j = 0; j < NUM_INTERIOR_WALLS; j++) {
				Point2f x = new Point2f();
				x.x = (float) Math.cos(orientation + j * 2 * Math.PI
						/ NUM_INTERIOR_WALLS)
						* NEAR_WALL_RADIUS;
				x.y = (float) Math.sin(orientation + j * 2 * Math.PI
						/ NUM_INTERIOR_WALLS)
						* NEAR_WALL_RADIUS;
				noClosePoints = noClosePoints
						&& univ.shortestDistanceToFeeders(x) > MIN_DIST_TO_FEEDERS;
				nearWall.add(x);
			}
		} while (!noClosePoints);

		// For each point, find a wall that is not too close to feeders and
		// not intersecting other walls
		for (Point2f seed : nearWall) {
			do {
				float orientation;
				Point2f translation, x2, x3;
				do {
					orientation = (float) (random.nextFloat() * 2 * Math.PI);

					translation = new Point2f();
					translation.x = (float) (LENGTH * Math.cos(orientation));
					translation.y = (float) (LENGTH * Math.sin(orientation));

					x2 = new Point2f(seed);
					x2.add(translation);
				} while (x2.distance(new Point2f()) > RADIUS);

				wall = new LineSegment(new Coordinate(seed.x, seed.y),
						new Coordinate(x2.x, x2.y));

			} while (univ.shortestDistanceToWalls(wall) == 0
					|| univ.wallDistanceToFeeders(wall) < MIN_DIST_TO_FEEDERS);

			univ.addWall(wall);

		}

		//
		// Add interior walls
		for (int i = 0; i < NUM_WALLS - NUM_INTERIOR_WALLS; i++) {
			Point2f firstPoint;
			LineSegment wall1;
			float orientation;
			Point2f secondPoint;
			Point2f thirdPoint;
			do {
				float x = random.nextFloat() - .5f;
				float y = random.nextFloat() - .5f;
				firstPoint = new Point2f(x, y);

				orientation = (float) (random.nextFloat() * 2 * Math.PI);

				Point2f translation = new Point2f();
				translation.x = (float) (LENGTH * Math.cos(orientation));
				translation.y = (float) (LENGTH * Math.sin(orientation));
				secondPoint = new Point2f(firstPoint);
				secondPoint.add(translation);
				wall1 = new LineSegment(new Coordinate(firstPoint.x,
						firstPoint.y), new Coordinate(secondPoint.x,
						secondPoint.y));

			} while (firstPoint.distance(new Point2f()) > RADIUS
					|| secondPoint.distance(new Point2f()) > RADIUS
					|| univ.shortestDistanceToWalls(wall1) < DISTANCE_INTERIOR_WALLS
					|| univ.shortestDistanceToFeeders(secondPoint) < MIN_DIST_TO_FEEDERS_INTERIOR);

			univ.addWall(wall1);

		}

	}

	public static void main(String[] args) {
//		for (int i = 0; i < 1000; i++){
			ElementWrapper root = XMLExperimentParser
					.loadRoot("src/edu/usf/ratsim/experiment/xml/multiFeedersTrainRecallSmallObs.xml");
			Universe univ = UniverseLoader.getInstance().load(root, ".");
			Robot robot = RobotLoader.getInstance().load(root);
			Subject subject = SubjectLoader.getInstance().load("a", "a",
					root.getChild("model"), robot);
			new AddSmallWallsTask(null).perform(univ, subject);
			System.out.println("walls added");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
//		}
		
	}
}
