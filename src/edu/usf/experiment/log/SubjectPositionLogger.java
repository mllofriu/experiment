package edu.usf.experiment.log;

import javax.vecmath.Point3f;

import edu.usf.experiment.Episode;
import edu.usf.experiment.Trial;
import edu.usf.experiment.robot.LocalizableRobot;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.ElementWrapper;

public class SubjectPositionLogger extends PositionLogger {

	public SubjectPositionLogger(ElementWrapper params) {
		super(params);
	}

	public void log(Subject sub) {
		if (!(sub.getRobot() instanceof LocalizableRobot))
			throw new RuntimeException("SubjectPositionLogger needs a localizable robot to work");
		
		LocalizableRobot r = (LocalizableRobot) sub.getRobot();
		Point3f pos = r.getPosition();
		addPose(new Pose(pos.x, pos.y, false));
	}
	
	@Override
	public void log(Trial trial) {
		log(trial.getSubject());
	}
	
	@Override
	public void log(Episode episode) {
		log(episode.getSubject());
	}

	@Override
	public String getFileName() {
		return "subjposition.csv";
	}

	@Override
	public String getHeader() {
		return "trial\tgroup\tsubject\trepetition\tx\ty\trandom";
	}

}
