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
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.utils.ElementWrapper;

public class AddLargeWallsTask extends Task {

	private final float RADIUS = .5f;
	private static final float MIN_DIST_TO_FEEDERS = 0.05f;
	private static final float NEAR_WALL_RADIUS = .49f;
	private static final float LENGTH = .4f;
	private static final int NUM_WALLS = 3;

	public AddLargeWallsTask(ElementWrapper params) {
		super(params);

	}

	@Override
	public void perform(Experiment experiment) {
		perform(experiment.getUniverse());
	}

	@Override
	public void perform(Trial trial) {
		perform(trial.getUniverse());
	}

	@Override
	public void perform(Episode episode) {
		perform(episode.getUniverse());
	}

	private void perform(Universe univ) {
		Random random = new Random();

		List<Point2f> nearWall;
		LineSegment wall, wall2;
		boolean noClosePoints;
		do {
			// Pick an orientation to seed the three walls near the outside
			float orientation = (float) (random.nextFloat() * Math.PI);
			nearWall = new LinkedList<Point2f>();
			noClosePoints = true;
			for (int j = 0; j < 3; j++) {
				Point2f x = new Point2f();
				x.x = (float) Math.cos(orientation + j * 2 * Math.PI / 3)
						* NEAR_WALL_RADIUS;
				x.y = (float) Math.sin(orientation + j * 2 * Math.PI / 3)
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
					translation.x = (float) (LENGTH / 2 * Math.cos(orientation));
					translation.y = (float) (LENGTH / 2 * Math.sin(orientation));

					x2 = new Point2f(seed);
					x2.add(translation);
				} while (x2.distance(new Point2f()) > RADIUS);

				do {
					float breakAngle;
					if (random.nextFloat() > .5)
						breakAngle = (float) (Math.PI / 3);
					else
						breakAngle = (float) (Math.PI / 4);
					if (random.nextFloat() > .5)
						breakAngle = -breakAngle;

					x3 = new Point2f(x2);
					translation.x = (float) (LENGTH / 2 * Math.cos(orientation
							+ breakAngle));
					translation.y = (float) (LENGTH / 2 * Math.sin(orientation
							+ breakAngle));
					x3.add(translation);

				} while (x3.distance(new Point2f()) > RADIUS);

				wall = new LineSegment(new Coordinate(seed.x, seed.y),
						new Coordinate(x2.x, x2.y));
				wall2 = new LineSegment(new Coordinate(x2.x, x2.y),
						new Coordinate(x3.x, x3.y));

			} while (univ.shortestDistanceToWalls(wall) == 0
					|| univ.shortestDistanceToWalls(wall2) == 0
					|| univ.wallDistanceToFeeders(wall) < MIN_DIST_TO_FEEDERS
					|| univ.wallDistanceToFeeders(wall2) < MIN_DIST_TO_FEEDERS);

			univ.addWall(wall);

			univ.addWall(wall2);

		}

	}

}
