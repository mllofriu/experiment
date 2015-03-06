package edu.usf.experiment.plot;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class PathPlotter extends Plotter {

	public PathPlotter(ElementWrapper params) {
		super(params);
	}

	@Override
	public void plot() {
		PropertyHolder props = PropertyHolder.getInstance();
		String logPath = props.getProperty("log.directory");
		IOUtils.copyResource(getClass().getResource("/edu/usf/experiment/plot/plotPath.r"), logPath + "plotPath.r");
		IOUtils.exec("Rscript plotPath.r", logPath);
	}

}
