package edu.usf.experiment.plot;

import java.net.URL;

import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class PlotGatherer extends Plotter {

	public PlotGatherer(ElementWrapper params, String logPath) {
		super(params, logPath);
	}

	@Override
	public void plot() {
		String logPath = getLogPath();
		IOUtils.exec("find . -iname '*.pdf' -exec ln {} plots/ ;", logPath);
		IOUtils.exec("find . -iname '*.png' -exec ln {} plots/ ;", logPath);
	}

}
