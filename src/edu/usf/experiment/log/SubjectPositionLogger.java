package edu.usf.experiment.log;

import javax.vecmath.Point3f;

import edu.usf.experiment.Episode;
import edu.usf.experiment.robot.LocalizableRobot;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.ElementWrapper;

public class SubjectPositionLogger extends PositionLogger {

	public SubjectPositionLogger(ElementWrapper params) {
		super(params);
	}

	@Override
	public void log(Episode episode) {
		Subject sub = episode.getSubject();
		if (!(sub.getRobot() instanceof LocalizableRobot))
			throw new RuntimeException("SubjectPositionLogger needs a localizable robot to work");
		
		LocalizableRobot r = (LocalizableRobot) sub.getRobot();
		Point3f pos = r.getPosition();
		addPose(new Pose(pos.x, pos.y, false));
	}

	@Override
	public String getFileName() {
		return "subjposition.txt";
	}

}
