package edu.usf.experiment.log;

import javax.vecmath.Point3f;

import edu.usf.experiment.Episode;
import edu.usf.experiment.utils.ElementWrapper;

public class UniversePositionLogger extends PositionLogger {

	public UniversePositionLogger(ElementWrapper params) {
		super(params);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void log(Episode episode) {
		Point3f pos = episode.getUniverse().getRobotPosition();
		addPose(new Pose(pos.x, pos.y, false));
	}

	@Override
	public String getFileName() {
		return "univposition.csv";
	}
	
	@Override
	public String getHeader() {
		return "trial\tgroup\tsubject\trepetition\tx\ty\trandom";
	}

}

