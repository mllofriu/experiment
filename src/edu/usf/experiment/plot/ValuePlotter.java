package edu.usf.experiment.plot;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class ValuePlotter extends Plotter {

	public ValuePlotter(ElementWrapper params) {
		super(params);
	}

	@Override
	public void plot() {
		PropertyHolder props = PropertyHolder.getInstance();
		String logPath = props.getProperty("log.directory");
		IOUtils.copyResource(getClass().getResource("/edu/usf/experiment/plot/plotValue.r"), logPath + "plotPath.r");
		IOUtils.exec("Rscript plotValue.r", logPath);
	}

}
