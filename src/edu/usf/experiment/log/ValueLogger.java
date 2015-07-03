package edu.usf.experiment.log;

import java.io.PrintWriter;

import javax.vecmath.Point3f;

import edu.usf.experiment.Episode;
import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.affordance.Affordance;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.utils.ElementWrapper;

public class ValueLogger extends Logger {

	private static final float MARGIN = 0.0f;
	private int numIntentions;
	private float angleInterval;
	private float interval;
	private boolean circle;

	public ValueLogger(ElementWrapper params) {
		super(params);

		numIntentions = params.getChildInt("numIntentions");
		angleInterval = params.getChildFloat("angleInterval");
		interval = params.getChildFloat("interval");
		circle = params.getChildBoolean("circle");
	}

	@Override
	public void log(Episode episode) {
		Universe univ = episode.getUniverse();
		Subject sub = episode.getSubject();
		PrintWriter writer = getWriter();

		PropertyHolder props = PropertyHolder.getInstance();
		String trialName = props.getProperty("trial");
		String groupName = props.getProperty("group");
		String subName = props.getProperty("subject");
		String episodeName = props.getProperty("episode");

		System.out.println("Starting to log value");

		for (int intention = 0; intention < numIntentions; intention++) {
			for (float xInc = MARGIN; xInc
					- (univ.getBoundingRectangle().getWidth() - MARGIN / 2) < 1e-8; xInc += interval) {
				for (float yInc = MARGIN; yInc
						- (univ.getBoundingRectangle().getHeight() - MARGIN / 2) < 1e-8; yInc += interval) {

					float x = (float) (univ.getBoundingRectangle().getMinX() + xInc);
					float y = (float) (univ.getBoundingRectangle().getMinY() + yInc);

					if (!circle
							|| inCircle(x, y, univ.getBoundingRectangle()
									.getWidth())) {
						float maxVal = sub.getValue(new Point3f(x, y, 0),
								intention, angleInterval);

						writer.println(trialName + '\t' + groupName + '\t'
								+ subName + '\t' + episodeName + '\t' + x
								+ "\t" + y + "\t" + intention + "\t" + maxVal);
					}
					System.out.print(".");
				}
			}
		}

		System.out.println("Finished loggin value");
	}

	private boolean inCircle(float x, float y, double width) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= width / 2;
	}

	@Override
	public void finalizeLog() {

	}

	@Override
	public String getHeader() {
		return "trial\tgroup\tsubject\trepetition\tx\ty\tintention\tval";
	}

	@Override
	public String getFileName() {
		return "value.csv";
	}

}
