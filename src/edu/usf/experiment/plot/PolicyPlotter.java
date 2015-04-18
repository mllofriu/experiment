package edu.usf.experiment.plot;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class PolicyPlotter extends Plotter {

	public PolicyPlotter(ElementWrapper params) {
		super(params);
	}

	@Override
	public void plot() {
		PropertyHolder props = PropertyHolder.getInstance();
		String logPath = props.getProperty("log.directory");
		IOUtils.copyResource(getClass().getResource("/edu/usf/experiment/plot/plotPolicy.r"), logPath + "plotPolicy.r");
		IOUtils.exec("Rscript plotPolicy.r", logPath);
	}

}