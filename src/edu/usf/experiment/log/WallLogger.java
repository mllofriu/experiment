package edu.usf.experiment.log;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import edu.usf.experiment.Episode;
import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.universe.Wall;
import edu.usf.experiment.utils.ElementWrapper;

public class WallLogger extends Logger {

	private List<Wall> walls;

	public WallLogger(ElementWrapper params) {
		super(params);

		walls = new LinkedList<Wall>();
	}

	@Override
	public void log(Episode episode) {
		for (Wall w : episode.getUniverse().getWalls())
			walls.add(w);
	}

	public String getFileName() {
		return "walls.csv";
	}

	@Override
	public void finalizeLog() {
		synchronized (WallLogger.class) {
			PropertyHolder props = PropertyHolder.getInstance();
			String trialName = props.getProperty("trial");
			String groupName = props.getProperty("group");
			String subName = props.getProperty("subject");
			String episode = props.getProperty("episode");
			
			PrintWriter writer = getWriter();
			for (Wall w : walls)
				writer.println(trialName + '\t' + groupName + '\t' + subName
						+ '\t' + episode + '\t' + w.getX1() + "\t" + w.getY1()
						+ '\t' + w.getX2() + "\t" + w.getY2());

			walls.clear();
		}
	}

	@Override
	public String getHeader() {
		return "trial\tgroup\tsubject\trepetition\tx\ty\txend\tyend";
	}

}
