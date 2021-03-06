package edu.usf.experiment.plot;

import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class RuntimePerGroupPlotter extends Plotter {

	public RuntimePerGroupPlotter(ElementWrapper params, String logPath) {
		super(params, logPath);
	}

	@Override
	public void plot() {
		String logPath = getLogPath();
		IOUtils.copyResource(getClass().getResource("/edu/usf/experiment/plot/plotRuntimesPerGroup.r"), logPath + "/plotRuntimesPerGroup.r");
		IOUtils.exec("Rscript plotRuntimesPerGroup.r", logPath);
	}

}
